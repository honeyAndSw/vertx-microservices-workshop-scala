package io.vertx.workshop.quote

import java.lang
import java.util.Objects

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.JsonObject
import io.vertx.workshop.common.Constants

import scala.util.Random

/**
  * A verticle simulating the evaluation of a company evaluation in a very unrealistic and irrational way.
  * It emits the new data on the `market` address on the event bus.
  *
  * Created by honey.and.sw on 2016. 10. 30..
  */
class MarketDataVerticle extends ScalaVerticle {

  private var marketData: MarketData = new MarketData

  /**
    * Method called when the verticle is deployed.
    */
  override def start(): Unit = {
    // Retrieve the configuration, and initialize the verticle.
    val config: JsonObject = ctx.config().get
    marketData = MarketData(config)

    // Every `period` ms, the given Handler is called.
    vertx.setPeriodic(marketData.period, delay => {
      marketData = marketData.newData
      send(marketData)
    })
  }

  private def send(data: MarketData): Unit = {
    vertx.eventBus().send(Constants.MarketEventAdress, data.toJson)
  }
}

class MarketData(val name: String,
                 val variation: Int,
                 val period: Long,
                 val symbol: String,
                 val stocks: Int,
                 val price: Double,
                 val bid: Double,
                 val ask: Double,
                 val share: Int,
                 val value: Double) {

  /**
    * Default Constructor
    */
  def this() {
    this("", 0, 0l, "", 0, 0.0, 0.0, 0.0, 0, 0.0)
  }

  def toJson: JsonObject = {
    new JsonObject()
      .put("exchange", "vert.x stock exchange")
      .put("symbol", symbol)
      .put("name", name)
      .put("bid", bid)
      .put("ask", ask)
      .put("volume", stocks)
      .put("open", price)
      .put("shares", share)
  }

  /**
    * Randomly compute bid, ask, share, and value
    * @return
    */
  def newData: MarketData = {
    val random = MarketData.random

    var (v: Double, a: Double, b: Double) = if (random.nextBoolean()) {
      (this.value + random.nextInt(variation),
       this.value + random.nextInt(variation / 2),
       this.value + random.nextInt(variation / 2))
    } else {
      (this.value - random.nextInt(variation),
       this.value - random.nextInt(variation / 2),
       this.value - random.nextInt(variation / 2))
    }

    if (v <= 0) {
      v = 1.0
    }
    if (a <= 0) {
      a = 1.0
    }
    if (b <= 0) {
      b = 1.0
    }

    val shareVariation: Int = random.nextInt(100)
    // Adjust share
    val s: Int = if (!random.nextBoolean()) {
      this.share
    } else {
      if (shareVariation > 0 && this.share + shareVariation < stocks) {
        this.share + shareVariation
      } else if (shareVariation < 0 && this.share + shareVariation > 0) {
        this.share + shareVariation
      } else {
        this.share
      }
    }

    new MarketData(name, variation, period, symbol, stocks, price, b, a, s, v)
  }
}

object MarketData {
  val random: Random = new Random()

  def apply(config: JsonObject): MarketData = {
    val name: String = config.getString("name")
    Objects.requireNonNull(name)

    val variation: Integer = config.getInteger("variation", 100)
    val stocks: Integer    = config.getInteger("volume", 10000)
    val price: lang.Double = config.getDouble("price", 100.0)

    new MarketData(name, variation, config.getLong("period", 3000L),
      config.getString("symbol", name), stocks, price,
      price + random.nextInt(variation / 2), price + random.nextInt(variation / 2),
      stocks / 2, price)
  }
}
