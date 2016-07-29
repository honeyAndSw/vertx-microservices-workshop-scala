import sbtassembly.MergeStrategy

lazy val `vertx-scala-sbt` = project
  .in(file("."))
    .settings(
      mainClass in assembly := Some("io.vertx.core.Launcher")
    )

libraryDependencies ++= Vector (
  Library.vertxLangScala,
  Library.vertxWeb,
  Library.vertxCodegen,
  Library.scalaTest       % "test"
)

initialCommands := """|import io.vertx.lang.scala._
                      |import io.vertx.scala.core._
                      |import io.vertx.scala.sbt._
                      |val vertx = Vertx.vertx
                      |""".stripMargin

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.last
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
  case PathList("codegen.json") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

