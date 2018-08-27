name := "Btg Scala Sending Generator"

scalaVersion := "2.10.6"

val sparkVersion = "2.0.0"

val quartzVersion = "2.2.1"

val akkaVersion = "2.3.16"

val mysqlConnectorVersion = "5.1.12"

mainClass in (Compile, run) := Some("main.scala.run.Main")

mainClass in (Compile, packageBin) := Some("main.scala.run.Main")

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "com.datastax.spark" % "spark-cassandra-connector_2.10" % sparkVersion,
    "org.apache.spark" % "spark-sql_2.10" % sparkVersion,
    "org.quartz-scheduler" % "quartz" % quartzVersion,
    "com.typesafe.akka" % "akka-actor_2.10" % akkaVersion,
    "mysql" % "mysql-connector-java" % mysqlConnectorVersion
 )