#PRECONDITIONS

clone and build: [Vert.x Scala Lang Module](https://github.com/codepitbull/vertx-lang-scala)

clone and buils: [Vert.x Scala Lang Stack](https://github.com/codepitbull/vertx-lang-scala-stack)

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