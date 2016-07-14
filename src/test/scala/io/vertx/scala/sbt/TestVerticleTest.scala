package io.vertx.scala.sbt

import io.vertx.core.AsyncResult
import io.vertx.core.eventbus.Message
import io.vertx.scala.core.Vertx
import org.scalatest._
import org.scalatest.concurrent.AsyncAssertions._
import org.scalatest.concurrent.AsyncAssertions.Waiter

import scala.concurrent.duration.DurationDouble

class TestVerticleTest extends FlatSpec with Matchers {

  "TestVerticle" should "reply to a message" in {
    val vertx = Vertx.vertx

    val w = new Waiter
    vertx.deployVerticle(classOf[TestVerticle].getName, res => {
      w { assert(res.succeeded())}
      w.dismiss()
    })
    w.await(timeout(50 millis))

    val w2 = new Waiter
    vertx.eventBus.send[String]("testAddress", "msg", (reply:AsyncResult[Message[String]]) => {
      w2 {assert("Hello World!" == reply.result().body())}
      w2.dismiss()
    })
    w2.await(timeout(50 millis))
  }
}
