package io.vertx.workshop.dashboard

import io.vertx.ext.web.handler.StaticHandler
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.http.HttpClient
import io.vertx.scala.ext.web.{Router, RoutingContext}
import io.vertx.workshop.common.MicroServiceVerticle

/**
  * Created by honey.and.sw on 2016. 11. 6.
  */
class DashboardVerticle extends MicroServiceVerticle {

  var client: HttpClient = null

  override def start(): Unit = {
    super.start()

    val router = Router.router(vertx)

    // Last operations
    router.get("/operations").handler(callAuditService)

    // Static content
    router.route("/*").asJava.handler(StaticHandler.create())

    vertx.createHttpServer()
      .requestHandler(router.accept _)
      .listen(8080)
  }

  private def callAuditService(context: RoutingContext): Unit = {
    if (client == null) {
      context.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(Json.obj(("message", "No audit service")).encode())
    } else {
      client.get("/", response => {
        response
          .exceptionHandler(context.fail _)
          .bodyHandler(buffer => {
            context.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(buffer)
        })
      }).exceptionHandler(context.fail _)
        .setTimeout(5000)
        .end()
    }
  }
}
