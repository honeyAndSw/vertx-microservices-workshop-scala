package io.vertx.workshop.trader

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.DeploymentOptions

/**
  * MainVerticle
  *
  * @author honey.and.sw
  * @since 2016. 12. 11.
  */
class MainVerticle extends ScalaVerticle {
  override def start(): Unit = {
    vertx.deployVerticle(s"scala:${classOf[CompulsiveTraderVerticle].getName}",
                          DeploymentOptions().setInstances(2))
  }
}