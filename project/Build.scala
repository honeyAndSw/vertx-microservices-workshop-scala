import org.scalafmt.sbt.ScalaFmtPlugin
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtassembly.PathList

object Build extends AutoPlugin {

  override def requires = ScalaFmtPlugin

  // Triggers this auto plugin automatically
  override def trigger = allRequirements

  /**
    * See also http://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html
    */
  override def projectSettings =
    // Enables ScalaFmtPlugin
    ScalaFmtPlugin.autoImport.reformatOnCompileSettings ++
    Vector(
      resolvers ++= Seq {
        "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
        "Sonatype SNAPSHOTS" at "https://oss.sonatype.org/content/repositories/snapshots/"
      },
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
      // mainClass := Some("io.vertx.core.Launcher"),
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
                           |""".stripMargin,
      assemblyMergeStrategy in assembly := {
        case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
        case PathList("META-INF", xs @ _*) => MergeStrategy.last
        case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
        case PathList("codegen.json") => MergeStrategy.discard
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },

      ScalaFmtPlugin.autoImport.scalafmtConfig := Some(baseDirectory.in(ThisBuild).value / ".scalafmt")
    )
}
