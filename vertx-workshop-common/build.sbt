import sbt.Package._

version := "0.1-SNAPSHOT"
name := "vertx-workshop-common"
organization := "io.vertx"

libraryDependencies ++= Vector (
  Library.vertxLangScala,
  Library.vertxCodegen,
  Library.vertxServiceDiscovery,
  Library.scalaTest       % "test"
)

packageOptions += ManifestAttributes(
  ("Main-Verticle", "scala:io.vertx.workshop.common.DemoVerticle"))



