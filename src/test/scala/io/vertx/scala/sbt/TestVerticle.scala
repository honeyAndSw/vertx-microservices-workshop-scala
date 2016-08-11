package io.vertx.scala.sbt

import io.vertx.core.Future
import io.vertx.lang.scala.ScalaVerticle

class TestVerticle extends ScalaVerticle {
  override def start(startFuture: Future[Void]): Unit = {
    vertx
      .eventBus()
      .consumer[String]("testAddress")
      .handler(_.reply("Hello World!"))
      .completionHandler(_ => startFuture.complete())
    val map = Map()

  }
}
