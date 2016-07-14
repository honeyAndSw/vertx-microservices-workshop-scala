package io.vertx.scala.sbt

import io.vertx.lang.scala.ScalaVerticle

class DemoVerticle extends ScalaVerticle {

  override def start(): Unit = {
    vertx
      .createHttpServer()
      .requestHandler(req => req.response().end("Hello World"))
      .listen(8666)
  }
}
