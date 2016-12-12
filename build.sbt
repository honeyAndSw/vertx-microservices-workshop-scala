import sbt.Package._

/**
  * @see http://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html
  */

val commonDependencies = Seq(
  Library.vertxLangScala,
  Library.vertxCodegen,
  Library.vertxServiceDiscovery,
  Library.scalaTest       % "test"
)

val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "io.vertx",
  scalaVersion := "2.12.0",
  libraryDependencies ++= commonDependencies,
  resolvers += Resolver.mavenLocal,
  resolvers += "Sonatype SNAPSHOTS" at "https://oss.sonatype.org/content/repositories/snapshots/",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", xs @ _*) => MergeStrategy.last
    case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
    case PathList("codegen.json") => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

def manifest(project: String): java.util.jar.Manifest = {
  val file = new java.io.File(project + "/src/META-INF/MANIFEST.MF")
  Using.fileInputStream(file)( in => new java.util.jar.Manifest(in) )
}

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
    name := "quote-generator",
    packageOptions += {
      Package.JarManifest(manifest("quote-generator"))
    }
  )

/* trader-dashboard */
lazy val dashboard = (project in file("trader-dashboard")).
  dependsOn(common).
  settings(commonSettings: _*).
  settings(
    name := "trader-dashboard",
    libraryDependencies += Library.vertxWeb,
    packageOptions += {
      Package.JarManifest(manifest("trader-dashboard"))
    }
  )

/* audit-service */
lazy val audit = (project in file("audit-service")).
   dependsOn(common).
   settings(commonSettings: _*).
   settings(
     name := "audit-service",
     libraryDependencies ++= Seq(Library.vertxJdbcClient, Library.hsqldb),
     packageOptions += {
       Package.JarManifest(manifest("audit-service"))
     }
   )

/* portfolio-service */
lazy val portfolio = (project in file("portfolio-service")).
  dependsOn(common).
  settings(commonSettings: _*).
  settings(
    name := "portfolio-service",
    libraryDependencies += Library.vertxServiceProxy,
    compileOrder := CompileOrder.JavaThenScala,
    /*
     * Add generated codes to Java source directory
     * @see http://www.scala-sbt.org/0.13/docs/Classpaths.html
     */
    unmanagedSourceDirectories in Compile += baseDirectory.value / "src" / "main" / "generated"
  )

/* compulsive-traders */
lazy val traders = (project in file("compulsive-traders")).
  dependsOn(common).
  dependsOn(portfolio).
  settings(commonSettings: _*).
  settings(
    name := "compulsive-traders"
  )