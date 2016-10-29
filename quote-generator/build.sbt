import sbt.Package._

version := "0.1-SNAPSHOT"
name := "quote-generator"
organization := "io.vertx"

libraryDependencies ++= Vector (
  Library.vertxLangScala,
  Library.vertxCodegen,
  Library.scalaTest       % "test"
)

packageOptions += ManifestAttributes(
  ("Main-Verticle", "scala:io.vertx.workshop.quote.DemoVerticle"))
