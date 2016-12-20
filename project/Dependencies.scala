import sbt._

object Version {
  final val Scala       = "2.12.0"
  final val ScalaTest   = "3.0.0"
  final val Vertx       = "3.4.0-SNAPSHOT"
  final val Hsqldb      = "2.3.3"
}

object Library {
  val vertxCodegen          = "io.vertx"      %  "vertx-codegen"                 % Version.Vertx     % "provided" changing() withSources()
  val vertxServiceProxy     = "io.vertx"      %  "vertx-service-proxy"           % Version.Vertx     changing() withSources()
  val vertxLangScala        = "io.vertx"      %% "vertx-lang-scala"              % Version.Vertx     changing() withSources()
  val vertxWeb              = "io.vertx"      %% "vertx-web-scala"               % Version.Vertx     changing() withSources()
  val vertxServiceDiscovery = "io.vertx"      %% "vertx-service-discovery-scala" % Version.Vertx     changing() withSources()
  val vertxJdbcClient       = "io.vertx"      %% "vertx-jdbc-client-scala"       % Version.Vertx     changing() withSources()
  val scalaTest             = "org.scalatest" %% "scalatest"                     % Version.ScalaTest changing()
  val hsqldb                = "org.hsqldb"    % "hsqldb"                         % Version.Hsqldb
}