package io.vertx.workshop.quote

import io.vertx.scala.core.DeploymentOptions
import io.vertx.core.json.JsonArray
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

    // Read the configuration, and deploy a MarketDataVerticle for each company listed in the configuration.
    val quotes: JsonArray = config.getJsonArray("companies")
    for {
      i <- 0 until quotes.size()
      company <- quotes.getJsonObject(i)
    } yield {
      // Deploy the verticle with a configuration.
      vertx.deployVerticle(classOf[MarketDataVerticle].getName, DeploymentOptions().setConfig(company))
    }
  }
}
