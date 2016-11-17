package io.vertx.workshop.common

/**
  * Created by honey.and.sw on 2016. 11. 5.
  */
object Constants {
  /** producer : MarketDataVerticle, which generates market data
    * consumer : RestQuoteAPIVerticle, which reads market data and provides REST API */
  val MarketEventAdress: String = "market"

  val PortfolioMessageName: String = "portfolio-events"
  val PortfolioAddress: String = "service.portfolio"
  /**
    * producer : PortfolioService
    * consumer :
    */
  val PortfolioEventAddress: String = "portfolio"

  /**
    * Helper to use PortfolioEventAddress in Java classes.
    * @return
    */
  def getEventAddress(): String = {PortfolioEventAddress}
}
