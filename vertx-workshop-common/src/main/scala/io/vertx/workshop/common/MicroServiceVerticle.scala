package io.vertx.workshop.common

import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.servicediscovery.{ServiceDiscoveryOptions, ServiceDiscovery}

/**
  * Created by naheon on 2016. 10. 28..
  */
class MicroServiceVerticle extends ScalaVerticle {

  protected var discovery: ServiceDiscovery = _

  override def start(): Unit = {
    discovery = ServiceDiscovery.create(vertx, ServiceDiscoveryOptions().setBackendConfiguration(config))
  }

  /**
    * ScalaVerticle doesn't support config().
    * @return
    */
  def config: JsonObject = ctx.config.get
}
