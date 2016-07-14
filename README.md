PRECONDITION: BUILD THE PULL-REQUEST FROM [HERE](https://github.com/vert-x3/vertx-lang-scala/pull/8)

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