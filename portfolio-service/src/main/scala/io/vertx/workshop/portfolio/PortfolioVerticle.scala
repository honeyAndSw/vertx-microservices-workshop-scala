package io.vertx.workshop.portfolio

import io.vertx.workshop.common.{Constants, MicroServiceVerticle}
import io.vertx.serviceproxy.ProxyHelper
import io.vertx.workshop.portfolio.impl.PortfolioServiceImpl

import scala.util.{Failure, Success}

/**
  * A verticle publishing the portfolio service.
  *
  * Created by honey.and.sw on 2016. 11. 17.
  */
class PortfolioVerticle extends MicroServiceVerticle {

  override def start() = {
    super.start()

    // Create the service object
    val service: PortfolioServiceImpl = new PortfolioServiceImpl(vertx.asJava, discovery.asJava, getConfiguration.getDouble("money", 10000.00))

    // Register the service proxy on the event bus
    // ProxyHelper.registerService(classOf[PortfolioService], vertx.asJava, service, Constants.PortfolioAddress)

    // The portfolio event service
    publishMessageSource("portfolio-events", Constants.PortfolioEventAddress, future => future.onComplete {
      case Success(record) => println(s"Portfolio Events service published : ${record.getName}")
      case Failure(cause) => println(s"${cause.getStackTrace}")
    })
  }
}