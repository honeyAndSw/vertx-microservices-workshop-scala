package io.vertx.workshop.common

import java.io.File
import java.util.Scanner

import io.vertx.core.impl.ConcurrentHashSet
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.servicediscovery.types.HttpEndpoint
import io.vertx.scala.servicediscovery.{Record, ServiceDiscovery, ServiceDiscoveryOptions}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by honey.and.sw on 2016. 10. 28..
  */
class MicroServiceVerticle extends ScalaVerticle {

  protected var discovery: ServiceDiscovery = null
  protected var registeredRecords = new ConcurrentHashSet[Record]()

  override def start(): Unit = {
    discovery = ServiceDiscovery
      .create(vertx, ServiceDiscoveryOptions().setBackendConfiguration(ctx.config().get))
  }

  def publishHttpEndpoint(name: String, host: String, port: Int, completionHandler: (Future[Record] => Unit)): Unit = {
    val record: Record = HttpEndpoint.createRecord(name, host, port, "/")
    publish(record, completionHandler)
  }

  /**
    * Vert.x Service Discovery
    * https://github.com/vert-x3/vertx-lang-scala/blob/master/vertx-lang-scala-stack/vertx-service-discovery-scala/src/main/asciidoc/scala/index.adoc#publishing-services
    * @param record
    * @param completionHandler
    */
  private def publish(record: Record,
                         completionHandler: (Future[Record] => Unit)): Unit = {
    // Create ServiceDiscovery if not exists.
    if (discovery == null) {
      try {
        start()
      } catch {
        case e: Throwable => throw new RuntimeException("Cannot create discovery service")
      }
    }

    discovery.publishFuture(record).onComplete {
      case Success(result) => {
        registeredRecords.add(record)
        completionHandler(Future.successful(result))
      }
      case Failure(cause) => {
        completionHandler(Future.failed(cause))
      }
    }
  }

  /**
    * Own implementation, which ScalaVerticle doesn't support.
    */
  def getConfiguration: JsonObject = getConfiguration("classes/conf/config.json")

  def getConfiguration(name: String): JsonObject =
    getConfiguration(new File(name))

  /**
    * Copied from {@link io.vertx.workshop.common.Launcher}
    * Originally intended to be called before every vertical is deployed.
    * [TODO] It should be merged to packaging configuration.
    *
    * @param config
    * @return
    */
  def getConfiguration(config: File): JsonObject = {
    if (config.isFile()) {
      println(s"Reading config file: ${ config.getAbsolutePath }")

      var scanner: Scanner = null
      var json: String     = null

      val conf: JsonObject = try {
        scanner = new Scanner(config).useDelimiter("\\A")
        json = scanner.next()
        Json.fromObjectString(json)
      } catch {
        case e: Exception => sys.error(s"Cannot read configuration file")
      } finally {
        scanner.close()
      }
      conf
    } else {
      println(s"Config file not found ${ config.getAbsolutePath }")
      new JsonObject()
    }
  }
}
