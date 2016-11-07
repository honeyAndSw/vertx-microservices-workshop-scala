package io.vertx.workshop.dashboard

import io.vertx.ext.web.handler.StaticHandler
import io.vertx.scala.ext.web.Router
import io.vertx.workshop.common.MicroServiceVerticle

/**
  * Created by honey.and.sw on 2016. 11. 6.
  */
class DashboardVerticle extends MicroServiceVerticle {

  override def start(): Unit = {
    super.start()

    val router = Router.router(vertx)

    // Static content
    router.route("/*").asJava.handler(StaticHandler.create())

    vertx.createHttpServer()
      .requestHandler(router.accept _)
      .listen(8080)
  }
}
