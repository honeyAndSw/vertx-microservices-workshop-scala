package io.vertx.scala.sbt

import io.vertx.core.http.HttpMethod
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.Router

class DemoVerticle extends ScalaVerticle {

  override def start(): Unit = {
    val router = Router.router(vertx)
    router
      .route(HttpMethod.GET, "/hello")
      .handler(req => req.response().end("world"))

    vertx.createHttpServer().requestHandler(router.accept).listen(8666)
  }
}
