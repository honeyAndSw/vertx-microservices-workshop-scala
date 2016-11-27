package io.vertx.workshop.quote

import io.vertx.lang.scala.json.{JsonArray, JsonObject}
import io.vertx.scala.core.DeploymentOptions
import io.vertx.scala.servicediscovery.Record
import io.vertx.workshop.common.MicroServiceVerticle

/**
  * Created by honey.and.sw on 2016. 10. 28.
  */
class GeneratorConfigVerticle extends MicroServiceVerticle {

  /**
    * This method is called when the verticle is deployed.
    */
  override def start(): Unit = {
    super.start()

    // Read the configuration
    val config: JsonObject = getConfiguration("classes/conf/config.json")
    val quotes: JsonArray  = config.getJsonArray("companies")
    println(s"[INITIAL QUOTES] ${ quotes }")

    // Deploy a MarketDataVerticle for each company listed in the configuration
    for (i <- 0 until quotes.size()) {
      val company: JsonObject = quotes.getJsonObject(i)
      vertx.deployVerticle(s"scala:${classOf[MarketDataVerticle].getName}",
                           DeploymentOptions().setConfig(company))
    }

    // Deploy another verticle without configuration.
    vertx.deployVerticle(s"scala:${classOf[RestQuoteAPIVerticle].getName}",
                         DeploymentOptions().setConfig(config))

    // Publish the services in the discovery infrastructure.
    publishHttpEndpoint("quotes", "localhost", config.getInteger("http.port", 8080), future => {
      future.onSuccess{
        case record: Record => println(s"Quotes (Rest endpoint) service published : ${record.getName}")
      }
      future.onFailure{
        case cause => println(s"${cause.getStackTrace}")
      }
    })
  }
}
