package io.vertx.workshop.common

import java.io.{ FileNotFoundException, File }
import java.util.Scanner

import io.vertx.core.json.{ DecodeException, JsonObject }
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.servicediscovery.{ ServiceDiscoveryOptions, ServiceDiscovery }

/**
  * Created by honey.and.sw on 2016. 10. 28..
  */
class MicroServiceVerticle extends ScalaVerticle {

  protected var discovery: ServiceDiscovery = _

  override def start(): Unit = {
    discovery = ServiceDiscovery
      .create(vertx, ServiceDiscoveryOptions().setBackendConfiguration(config))
  }

  /**
    * ScalaVerticle doesn't support config().
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
