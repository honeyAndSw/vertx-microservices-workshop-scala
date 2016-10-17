package io.vertx.scala.sbt

import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.scala.core.Vertx
import org.scalatest._
import org.scalatest.concurrent.AsyncAssertions._
import org.scalatest.concurrent.AsyncAssertions.Waiter

import scala.concurrent.duration.DurationDouble
import scala.util.{Failure, Success}

class TestVerticleTest extends FlatSpec with Matchers with Assertions {

  "TestVerticle" should "reply to a message" in {
    val vertx = Vertx.vertx
    implicit val vertxExecutionContext = VertxExecutionContext(vertx.getOrCreateContext())

    val w = new Waiter
    vertx.deployVerticleFuture("scala:"+classOf[TestVerticle].getName).andThen {
      case Success(deplyomentId) => {
          w{assert(true)}
          w.dismiss()
        }
      case Failure(t) => {
          w{ fail(t) }
          w.dismiss()
        }
    }
    w.await(timeout(50 millis))

    val w2 = new Waiter
    vertx
      .eventBus()
      .sendFuture[String]("testAddress", "msg").andThen{
        case Success(answer) => {
            w2 { assert("Hello World!" == answer.body()) }
            w2.dismiss()
          }
        case Failure(t) => {
            w2 { fail(t) }
            w2.dismiss()
          }
      }
    w2.await(timeout(50 millis))
  }
}
