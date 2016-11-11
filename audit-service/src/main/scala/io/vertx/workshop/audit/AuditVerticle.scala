package io.vertx.workshop.audit

import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.eventbus.MessageConsumer
import io.vertx.scala.core.http.HttpServer
import io.vertx.workshop.common.MicroServiceVerticle

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
  * Created by honey.and.sw on 2016. 11. 10.
  */
class AuditVerticle extends MicroServiceVerticle {
  override def start(startPromise: Promise[Unit]) = {

    val databaseReady: Future[Unit] = initializeDatabase(true)
    var httpEndpointReady: Future[HttpServer] = configureTheHTTPServer()
    var messageListenerReady: Future[MessageConsumer[JsonObject]] = retrieveThePortfolioMessageSource()

    val ready: Future[MessageConsumer[JsonObject]] = for {
      database <- databaseReady
      httpEndpoint <- httpEndpointReady
      messageListener <- messageListenerReady
    } yield {
      messageListener
    }

    ready.onComplete {
      case Success(consumer) => {
        consumer.handler(message => ???)
        startPromise.success()
      }
      case Failure(cause) => {
        startPromise.failure(cause)
      }
    }

  }

  private def initializeDatabase(drop: Boolean): Future[Unit] = {
    val future: Future[Unit] = Future{}
    future
  }

  private def configureTheHTTPServer(): Future[HttpServer] = {
    val future: Future[HttpServer] = Future{???}
    future
  }

  private def retrieveThePortfolioMessageSource(): Future[MessageConsumer[JsonObject]] = {
    val future: Future[MessageConsumer[JsonObject]] = Future{???}
    future
  }
}
