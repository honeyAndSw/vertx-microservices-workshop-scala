package io.vertx.workshop.trader

import io.vertx.lang.scala.json.Json
import io.vertx.scala.servicediscovery.types.MessageSource
import io.vertx.scala.servicediscovery.types.EventBusService
import io.vertx.workshop.common.{Constants, MicroServiceVerticle}
import io.vertx.workshop.portfolio.PortfolioService

import scala.concurrent.Future

/**
  * CompulsiveTraderVerticle
  *
  * @author honey.and.sw
  * @since 2016. 12. 11.
  */
class CompulsiveTraderVerticle extends MicroServiceVerticle {
  override def start(startPromise: concurrent.Promise[Unit]): Unit = {
    // Initialize ServiceDiscovery
    super.start()

    val company: String = TraderUtils.pickACompany
    val numberOfShares: Int = TraderUtils.pickANumber
    println(s"Compulsive trader configured for company ${company} and shares ${numberOfShares}")

    val proxyFuture: Future[PortfolioService] = EventBusService.getProxyFuture(discovery, "io.vertx.workshop.portfolio.PortfolioService")
    val messageConsuerFuture = MessageSource.getConsumerFuture(discovery, Json.obj(("name", Constants.MarketMessageName)))

    startPromise.success()
  }
}
