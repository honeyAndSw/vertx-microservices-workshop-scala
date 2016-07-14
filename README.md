PRECONDITION: BUILD THE PULL-REQUEST FROM

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