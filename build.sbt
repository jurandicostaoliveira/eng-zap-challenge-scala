name := "Btg Scala Sending Generator"

scalaVersion := "2.10.6"
val sparkVersion = "2.0.0"
val quartzVersion = "2.2.1"
val akkaVersion = "2.3.16"
val mysqlConnectorVersion = "5.1.12"
val log4jVersion = "11.0"
val redisVersion = "3.8"
val json4sVersion = "3.3.0"

//mainClass in(Compile, run) := Some("main.scala.run.Main")
//mainClass in(Compile, packageBin) := Some("main.scala.run.Main")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "com.datastax.spark" % "spark-cassandra-connector_2.10" % sparkVersion,
  "org.apache.spark" % "spark-sql_2.10" % sparkVersion,
  "org.quartz-scheduler" % "quartz" % quartzVersion,
  "com.typesafe.akka" % "akka-actor_2.10" % akkaVersion,
  "mysql" % "mysql-connector-java" % mysqlConnectorVersion,
  "org.apache.logging.log4j" % "log4j-scala" % log4jVersion,
  "net.debasishg" %% "redisclient" % redisVersion, //https://github.com/debasishg/scala-redis
  "org.json4s" %% "json4s-native" % json4sVersion, //https://github.com/json4s/json4s
  "com.google.code.gson" % "gson" % "1.7.1",
  "commons-validator" % "commons-validator" % "1.4.1"
)