package io.vertx.workshop.audit

import io.vertx.lang.scala.json.{JsonObject}
import io.vertx.scala.core.eventbus.MessageConsumer
import io.vertx.scala.core.http.HttpServer
import io.vertx.workshop.common.MicroServiceVerticle

import scala.concurrent.{Future, Promise}

/**
  * Created by honey.and.sw on 2016. 11. 10.
  */
class AuditVerticle extends MicroServiceVerticle {
  override def start(startPromise: Promise[Unit]) = {

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
