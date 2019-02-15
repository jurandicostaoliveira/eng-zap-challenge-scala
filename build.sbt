name := "Btg-Scala-Sending-Generator"

scalaVersion := "2.10.6"
val sparkVersion = "2.0.0"

mainClass in(Compile, run) := Some("br.com.btg360.scheduling.Kernel")
mainClass in(Compile, packageBin) := Some("br.com.btg360.scheduling.Kernel")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "com.datastax.spark" % "spark-cassandra-connector_2.10" % sparkVersion,
  "org.apache.spark" % "spark-sql_2.10" % sparkVersion,
  "org.quartz-scheduler" % "quartz" % "2.2.1",
  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.16",
  "mysql" % "mysql-connector-java" % "5.1.12",
  "org.apache.logging.log4j" % "log4j-scala" % "11.0",
  "net.debasishg" %% "redisclient" % "3.8", //https://github.com/debasishg/scala-redis
  "org.json4s" %% "json4s-native" % "3.3.0", //https://github.com/json4s/json4s
  "commons-validator" % "commons-validator" % "1.4.1"
)