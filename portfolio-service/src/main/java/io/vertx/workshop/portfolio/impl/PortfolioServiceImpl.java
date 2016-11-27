package io.vertx.workshop.portfolio.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.workshop.portfolio.Portfolio;
import io.vertx.workshop.portfolio.PortfolioService;

/**
 * Created by honey.and.sw on 2016. 11. 18.
 */
public class PortfolioServiceImpl implements PortfolioService {

    private final Vertx vertx;
    private final Portfolio portfolio;
    private final ServiceDiscovery discovery;

    public PortfolioServiceImpl(Vertx vertx, ServiceDiscovery discovery, double initialCash) {
        this.vertx = vertx;
        this.portfolio = new Portfolio().setCash(initialCash);
        this.discovery = discovery;
    }

    @Override
    public void getPortfolio(Handler<AsyncResult<Portfolio>> resultHandler) {
        resultHandler.handle(Future.succeededFuture(portfolio));
    }

    /**
     * Send operations(BUY, SELL) to event bus.
     *
     * @param action
     * @param amount
     * @param quote
     * @param newAmount
     */
    private void sendActionOnTheEventBus(String action, int amount, JsonObject quote, int newAmount) {
        JsonObject object = new JsonObject()
                .put("action", action)
                .put("amount", amount)
                .put("quote", quote)
                .put("owned", newAmount)
                .put("date", System.currentTimeMillis());
        vertx.eventBus().publish("portfolio", object);
    }

    @Override
    public void buy(int amount, JsonObject quote, Handler<AsyncResult<Portfolio>> resultHandler) {

    }

    @Override
    public void sell(int amount, JsonObject quote, Handler<AsyncResult<Portfolio>> resultHandler) {

    }

    @Override
    public void evaluate(Handler<AsyncResult<Double>> resultHandler) {

    }
}
