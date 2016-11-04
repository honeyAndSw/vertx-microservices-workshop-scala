package io.vertx.workshop.quote

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.http.{HttpServerResponse, HttpServerRequest, HttpServer}

import scala.util.{Failure, Success}

/**
  * Created by honey.and.sw on 2016. 11. 2.
  */
class RestQuoteAPIVerticle extends ScalaVerticle {

  override def start(): Unit = {
    val port: Int = ctx.config().get.getInteger("http.port", 8080)

    // Create a HTTP server that returns the quotes
    vertx.createHttpServer()
      .requestHandler(request => {
        var response: HttpServerResponse = request.response().putHeader("content-type", "application/json")

        // val company: Option[String] = request.getParam("name")
        request.getParam("name") match {
          case Some(company) => ???
          case None => ???
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