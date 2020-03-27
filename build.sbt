name := "Btg-Scala-Sending-Generator"
version := "0.1"

val scalaGeneralVersion = "2.11.8" //"2.10.6"
scalaVersion := scalaGeneralVersion
val sparkVersion = "2.3.3" //"2.0.0"
val entryPoint = Some("br.com.btg360.scheduling.Kernel")

mainClass in(Compile, run) := entryPoint
mainClass in(Compile, packageBin) := entryPoint

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.datastax.spark" %% "spark-cassandra-connector" % sparkVersion,
  "org.quartz-scheduler" % "quartz" % "2.2.1",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.21",
  "mysql" % "mysql-connector-java" % "5.1.46",
  "org.apache.logging.log4j" % "log4j-scala" % "11.0",
  "net.debasishg" %% "redisclient" % "3.8", //https://github.com/debasishg/scala-redis
  //"org.json4s" %% "json4s-native" % "3.3.0", //https://github.com/json4s/json4s
  "commons-validator" % "commons-validator" % "1.4.1",
  "com.m3" %% "curly-scala" % "0.5.+"
)

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "br.com.btg360",
  scalaVersion := scalaGeneralVersion,
  test in assembly := {}
)

lazy val app = (project in file("app")).
  settings(commonSettings: _*).settings(
  mainClass in assembly := entryPoint
)

assemblyMergeStrategy in assembly := {
  case PathList("org", "aopalliance", xs@_*) => MergeStrategy.last
  case PathList("java", "util", xs@_*) => MergeStrategy.last
  case PathList("javax", "inject", xs@_*) => MergeStrategy.last
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.last
  case PathList("javax", "activation", xs@_*) => MergeStrategy.last
  case PathList("org", "apache", xs@_*) => MergeStrategy.last
  case PathList("com", "google", xs@_*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last
  case PathList("com", "codahale", xs@_*) => MergeStrategy.last
  case PathList("com", "yammer", xs@_*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "git.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}