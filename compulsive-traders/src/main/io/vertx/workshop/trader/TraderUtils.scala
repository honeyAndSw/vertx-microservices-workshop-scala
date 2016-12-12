package main.io.vertx.workshop.trader

import io.vertx.lang.scala.json.JsonObject
import io.vertx.workshop.portfolio.PortfolioService

import scala.util.Random

/**
  * TraderUtils
  * A small utility class to initialize the compulsive traders and implement the stupid trading logic.
  *
  * @author honey.and.sw
  * @since 2016. 12. 11.
  */
object TraderUtils {

  private val RANDOM: Random = new Random()

  def pickACompany: String = {
    RANDOM.nextInt(2) match {
      case 0 => "Divinator"
      case 1 => "MacroHard"
      case _ => "Black Coat"
    }
  }

  def timeToSell: Boolean = RANDOM.nextBoolean()

  def pickANumber: Int = RANDOM.nextInt(6) + 1

  def dumbTradingLogic(company: String, numberOfShares: Int, portfolio: PortfolioService, quote: JsonObject): Unit = ???
}
