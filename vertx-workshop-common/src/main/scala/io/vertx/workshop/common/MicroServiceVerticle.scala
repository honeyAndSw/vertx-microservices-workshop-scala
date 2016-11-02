package io.vertx.workshop.common

import java.io.{ FileNotFoundException, File }
import java.util.Scanner

import io.vertx.core.AsyncResult
import io.vertx.core.impl.ConcurrentHashSet
import io.vertx.core.json.{ DecodeException, JsonObject }
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.servicediscovery.types.HttpEndpoint
import io.vertx.scala.servicediscovery.{Record, ServiceDiscoveryOptions, ServiceDiscovery}

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
      .create(vertx, ServiceDiscoveryOptions().setBackendConfiguration(config))
  }

  def publishHttpEndpoint[T](name: String, host: String, port: Int, completionHandler: (Future[T] => Unit)): Unit = {
    val record: Record = HttpEndpoint.createRecord(name, host, port, "/")
    publish(record, completionHandler)
  }

  /**
    * Vert.x Service Discovery
    * https://github.com/vert-x3/vertx-lang-scala/blob/master/vertx-lang-scala-stack/vertx-service-discovery-scala/src/main/asciidoc/scala/index.adoc#publishing-services
    * @param record
    * @param completionHandler
    * @tparam T
    */
  private def publish[T](record: Record, completionHandler: (Future[T] => Unit)): Unit = {
    // Create ServiceDiscovery if not exists.
    if (discovery == null) {
      try {
        start()
      } catch {
        case e => throw new RuntimeException("Cannot create discovery service")
      }
    }

    discovery.publishFuture(record).onComplete{
      case Success(result: T) => {
        registeredRecords.add(record)
        completionHandler(Future.successful[T](result))
      }
      case Failure(cause) => {
        completionHandler(Future.failed(cause))
      }
    }
  }

  /**
    * Own implementation, which ScalaVerticle doesn't support.
    * @return
    */
  def config: JsonObject = ctx.config().get

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
        new JsonObject(json)
      } catch {
        case e: DecodeException =>
          sys.error(
              s"Configurationf file ${ json } does not contain a valid JSON object"
          )
        case e: FileNotFoundException =>
          sys.error(s"Config file not found ${ config.getAbsolutePath }")
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
