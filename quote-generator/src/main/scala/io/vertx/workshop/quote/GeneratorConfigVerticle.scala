package io.vertx.workshop.quote

import java.io.{ FileNotFoundException, File }
import java.util.Scanner

import io.vertx.scala.core.DeploymentOptions
import io.vertx.core.json.{ DecodeException, JsonObject, JsonArray }
import io.vertx.workshop.common.MicroServiceVerticle

/**
  * Created by honey.and.sw on 2016. 10. 28..
  */
class GeneratorConfigVerticle extends MicroServiceVerticle {

  /**
    * This method is called when the verticle is deployed.
    */
  override def start(): Unit = {
    super.start()

    // Read the configuration
    val confFile: File     = new File("classes/conf/config.json")
    val config: JsonObject = getConfiguration(confFile)
    val quotes: JsonArray  = config.getJsonArray("companies")
    println(s"[INITIAL QUOTES] ${ quotes }")

    // Deploy a MarketDataVerticle for each company listed in the configuration
    for (i <- 0 until quotes.size()) {
      val company: JsonObject = quotes.getJsonObject(i)
      vertx.deployVerticle("scala:" + classOf[MarketDataVerticle].getName,
                           DeploymentOptions().setConfig(company))
    }
  }

  /**
    * Copied from {@link io.vertx.workshop.common.Launcher}
    * Originally intended to be called before every vertical is deployed.
    * [TODO] It should be merged to packaging configuration.
    *
    * @param config
    * @return
    */
  private def getConfiguration(config: File): JsonObject = {
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
