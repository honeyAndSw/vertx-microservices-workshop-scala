package io.vertx.workshop.quote

import io.vertx.core.json.{Json, JsonObject}
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.core.http.{HttpServer, HttpServerResponse}

import scala.collection.immutable.HashMap
import scala.util.{Failure, Success}

/**
  * Created by honey.and.sw on 2016. 11. 2.
  */
class RestQuoteAPIVerticle extends ScalaVerticle {

  private var quotes: Map[String, JsonObject] = new HashMap[String, JsonObject]()

  override def start(): Unit = {

    vertx.eventBus().consumer[JsonObject](Constants.MarketEventAdress, (message: Message[JsonObject]) => {
      val jsonObject = message.body()
      quotes = quotes + ((jsonObject.getString("name"), jsonObject))
    })

    val port: Int = ctx.config().get.getInteger("http.port", 8080)

    // Create a HTTP server that returns the quotes
    vertx.createHttpServer()
      .requestHandler(request => {
        var response: HttpServerResponse = request.response().putHeader("content-type", "application/json")

        // val company: Option[String] = request.getParam("name")
        request.getParam("name") match {
          case Some(company) => {
            if (quotes.contains(company)) {
              response.end(quotes(company).encodePrettily())
            } else {
              response.setStatusCode(404).end()
            }
          }
          case None => {
            val toJson = new JsonObject()
            response.end(Json.encodePrettily(quotes))
          }
        }
      })
      .listenFuture(port)
      .onComplete({
        case Success(result: HttpServer) => println(s"Server started on port ${result.actualPort()}")
        case Failure(cause) => println(s"Cannot start the server: ${cause.getMessage}")
      }
    )
  }
}