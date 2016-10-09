#Getting vertx-lang-scala

The current version in master is deployed as SNAPSHOTS in a sonatype repo (which is the default one configured fir this build)

#Get the vertx-modules

There is a separate project for generating other vertx-modules (vertx-web, vertx-auth, ...). It's currently untested but most modules should work.
If you want to try them out clone and build: [Vert.x Scala Lang Stack](https://github.com/codepitbull/vertx-lang-scala-stack)

#Work with this project

Create a runnable fat-jar
```
sbt assembly
```

play around in sbt
```
sbt
> console
scala> vertx.deployVerticle(classOf[DemoVerticle].getName)
scala> vertx.deploymentIDs
```

From here you can freely interact with the Vertx-API inside the sbt-scala-shell.