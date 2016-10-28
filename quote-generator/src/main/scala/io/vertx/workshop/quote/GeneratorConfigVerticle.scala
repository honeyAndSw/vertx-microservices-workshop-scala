package io.vertx.workshop.quote

import io.vertx.core.json.JsonArray
import io.vertx.workshop.common.MicroServiceVerticle

/**
  * Created by naheon on 2016. 10. 28..
  */
class GeneratorConfigVerticle extends MicroServiceVerticle {

  override def start(): Unit = {
    super.start()

    // Read the configuration, and deploy a MarketDataVerticle for each company listed in the configuration.
    val quotes: JsonArray = config.getJsonArray("companies")

  }
}
