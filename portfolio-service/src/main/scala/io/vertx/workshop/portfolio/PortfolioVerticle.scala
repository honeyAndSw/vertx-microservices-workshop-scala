package io.vertx.workshop.portfolio

import io.vertx.lang.scala.json.Json
import io.vertx.serviceproxy.ProxyHelper
import io.vertx.workshop.common.{Constants, MicroServiceVerticle}
import io.vertx.workshop.portfolio.impl.PortfolioServiceImpl

import scala.util.{Failure, Success}

/**
  * A verticle publishing the portfolio service.
  *
  * Created by honey.and.sw on 2016. 11. 17.
  */
class PortfolioVerticle extends MicroServiceVerticle {

  override def start(): Unit = {
    super.start()

    // Create the service object
    val service: PortfolioServiceImpl = new PortfolioServiceImpl(vertx.asJava, discovery.asJava, getConfiguration.getDouble("money", 10000.00))

    // Register the service proxy on the event bus
    ProxyHelper.registerService(classOf[PortfolioService], vertx.asJava, service, Constants.PortfolioAddress)

    // Publish it in the discovery infrastructure
    val metadata = Json.emptyObj()
    publishEventBusService("portfolio", Constants.PortfolioAddress, "io.vertx.workshop.portfolio.PortfolioService", metadata, future => future.onComplete {
      case Success(record) => println(s"portfolio published : ${record.getName}")
      case Failure(cause) => println(s"${cause.getStackTrace}")
    })

    // The portfolio event service
    // Corresponding messages will be created at PortfolioService
    publishMessageSource(Constants.PortfolioMessageName, Constants.PortfolioEventAddress, future => future.onComplete {
      case Success(record) => println(s"portfolio-events published : ${record.getName}")
      case Failure(cause) => println(s"${cause.getStackTrace}")
    })
  }
}