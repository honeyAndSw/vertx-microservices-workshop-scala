import sbt._

object Version {
  final val Scala           = "2.11.8"
  final val ScalaTest       = "2.2.6"
  final val Vertx           = "3.3.1"
  final val VertxLangScala  = "3.0.0-SNAPSHOT"
}

object Library {
  val vertxLangScala = "io.vertx"       %% "vertx-lang-scala" % Version.VertxLangScala
  val scalaTest      = "org.scalatest"  %% "scalatest"        % Version.ScalaTest
}
