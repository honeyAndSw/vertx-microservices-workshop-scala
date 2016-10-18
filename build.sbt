import sbt.Package._

version := "0.1-SNAPSHOT"
name := "vertx-scala-sbt"
organization := "io.vertx"

libraryDependencies ++= Vector (
  Library.vertxLangScala,
  Library.vertxCodegen,
  Library.scalaTest       % "test"
)

packageOptions += ManifestAttributes(
  ("Main-Verticle", "scala:io.vertx.scala.sbt.DemoVerticle"))



