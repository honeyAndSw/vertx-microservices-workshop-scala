package io.vertx.workshop.common

import java.io.{ File, FileNotFoundException }
import java.util.Scanner

import io.vertx.core.json.{ DecodeException, JsonObject }
import io.vertx.core.{ DeploymentOptions, VertxOptions }

/**
  * Created by honey.and.sw on 2016. 10. 31.
  */
class Launcher extends io.vertx.core.Launcher {
  override def beforeStartingVertx(options: VertxOptions): Unit = {
    options.setClustered(true).setClusterHost("127.0.0.1")
  }

  override def beforeDeployingVerticle(
      deploymentOptions: DeploymentOptions
  ): Unit = {
    super.beforeDeployingVerticle(deploymentOptions)

    if (deploymentOptions.getConfig == null) {
      deploymentOptions.setConfig(new JsonObject())

    }

    val conf: File = new File("classes/conf/config.json")
    deploymentOptions.getConfig().mergeIn(getConfiguration(conf))
  }

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
