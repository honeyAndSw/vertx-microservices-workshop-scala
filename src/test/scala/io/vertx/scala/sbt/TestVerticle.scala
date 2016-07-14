package io.vertx.scala.sbt

import io.vertx.core.Future
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

class TestVerticle extends ScalaVerticle{
  override def start(startFuture: Future[Void]): Unit = {
    vertx.eventBus()
      .consumer("testAddress")
      .handler((in:Message[String]) => in.reply("Hello World!"))
      .completionHandler(_ => startFuture.complete())
  }
}
