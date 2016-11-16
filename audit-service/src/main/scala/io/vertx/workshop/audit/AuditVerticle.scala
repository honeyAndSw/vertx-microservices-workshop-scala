package io.vertx.workshop.audit

import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.eventbus.MessageConsumer
import io.vertx.scala.core.http.HttpServer
import io.vertx.scala.ext.jdbc.JDBCClient
import io.vertx.scala.ext.sql.SQLConnection
import io.vertx.scala.ext.web.{RoutingContext, Router}
import io.vertx.workshop.common.MicroServiceVerticle

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
  * Created by honey.and.sw on 2016. 11. 10.
  */
class AuditVerticle extends MicroServiceVerticle {

  private val DROP_STATEMENT = "DROP TABLE IF EXISTS AUDIT"
  private val CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS AUDIT (id INTEGER IDENTITY, operation varchar(250))";

  private var jdbc: JDBCClient = null

  /**
    * Starts the verticle asynchronously. The the initialization is completed, it calls
    * `complete()` on the given {@link Future} object. If something wrong happens,
    * `fail` is called.
    *
    * @param startPromise the future to indicate the completion
    */
  override def start(startPromise: Promise[Unit]) = {

    // creates the jdbc client.
    val config = getConfiguration
    jdbc = JDBCClient.createShared(vertx, getConfiguration)

    val databaseReady: Future[Unit] = initializeDatabase(true)
    val httpEndpointReady: Future[HttpServer] = configureTheHTTPServer()
    val messageListenerReady: Future[MessageConsumer[JsonObject]] = retrieveThePortfolioMessageSource()

    val ready: Future[MessageConsumer[JsonObject]] = for {
      database <- databaseReady
      httpEndpoint <- httpEndpointReady
      messageListener <- messageListenerReady
    } yield {
      messageListener
    }

    ready.onComplete {
      case Success(consumer) => {
        // Register the handle called on messages
        consumer.handler(message => ???)
        startPromise.success()
      }
      case Failure(cause) => {
        // Notify the completion
        startPromise.failure(cause)
      }
    }
  }

  private def initializeDatabase(drop: Boolean): Future[Unit] = {
    val connectionRetrieved: Future[SQLConnection] = jdbc.getConnectionFuture()

    // The database initialization is a multi-step process:
    val databaseReady: Future[Unit] = for {
      connection <- connectionRetrieved   // 1. Retrieve the connection
      drop <- dropTable(connection, drop) // 2. Drop the table is exist
      create <- createTable(connection)   // 3. Create the table
    } yield {
      if (connectionRetrieved.isCompleted) {
        connection.close()                // 4. Close the connection (in any case)
      }
    }

    databaseReady
  }

  private def dropTable(connection: SQLConnection, drop: Boolean): Future[Unit] = {
    if (drop) connection.executeFuture(DROP_STATEMENT)
    else Future{}
  }

  private def createTable(connection: SQLConnection): Future[Unit] = connection.executeFuture(CREATE_TABLE_STATEMENT)

  private def configureTheHTTPServer(): Future[HttpServer] = {
    val router = Router.router(vertx)
    router.get("/").handler(retrieveOperations)

    vertx.createHttpServer()
      .requestHandler(router.accept _)
      .listenFuture(8080)
      .map(server => server)
  }

  private def retrieveOperations(context: RoutingContext): Unit = {

  }

  private def retrieveThePortfolioMessageSource(): Future[MessageConsumer[JsonObject]] = {
    val future: Future[MessageConsumer[JsonObject]] = Future{null}
    future
  }
}
