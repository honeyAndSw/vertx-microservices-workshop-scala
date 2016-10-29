import sbt.Package._

val commonDependencies = Seq(
  Library.vertxLangScala,
  Library.vertxCodegen,
  Library.vertxServiceDiscovery,
  Library.scalaTest       % "test"
)

val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "io.vertx",
  libraryDependencies ++= commonDependencies
)

/* vertx-workshop-common */
lazy val common = (project in file("vertx-workshop-common")).
  settings(commonSettings: _*).
  settings(
    name := "vertx-workshop-common"
  )

/* quote-generator */
lazy val quote = (project in file("quote-generator")).
  dependsOn(common).
  settings(commonSettings: _*).
  settings(
    name := "quote-generator"
  )

/* packageOptions += ManifestAttributes(
  ("Main-Verticle", "scala:io.vertx.workshop.quote.GeneratorConfigVerticle"))*/
