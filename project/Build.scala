import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtassembly.PathList

object Build extends AutoPlugin {
  // Triggers this auto plugin automatically
  override def trigger = allRequirements

  /**
    * See also http://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html
    */
  override def projectSettings =
    Vector(
      version := version.in(ThisBuild).value, 
      scalaVersion := Version.Scala,
      crossScalaVersions := Vector(scalaVersion.value),
      scalacOptions ++= Vector(
        "-unchecked",
        "-deprecation",
        "-language:_",
        "-target:jvm-1.8",
        "-encoding", "UTF-8"
      ),
      unmanagedSourceDirectories in Compile := Vector(scalaSource.in(Compile).value),
      unmanagedSourceDirectories in Test := Vector(scalaSource.in(Test).value),
      initialCommands := """|import io.vertx.lang.scala._
                           |import io.vertx.scala.core._
                           |import scala.concurrent.Future
                           |import scala.concurrent.Promise
                           |import scala.util.Success
                           |import scala.util.Failure
                           |val vertx = Vertx.vertx
                           |implicit val executionContext = io.vertx.lang.scala.VertxExecutionContext(vertx.getOrCreateContext)
                           |""".stripMargin
    )
}
