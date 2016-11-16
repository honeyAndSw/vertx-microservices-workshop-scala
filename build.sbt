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
  libraryDependencies ++= commonDependencies,
  resolvers += Resolver.mavenLocal,
  resolvers += "Sonatype SNAPSHOTS" at "https://oss.sonatype.org/content/repositories/snapshots/"
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