name := "Eng-Zap-Challenge-Scala"
version := "0.1"

val scalaGeneralVersion = "2.12.12"
scalaVersion := scalaGeneralVersion
val sparkVersion = "3.0.1"
val entryPoint = Some("br.com.zap.controller.MainController")

mainClass in(Compile, run) := entryPoint
mainClass in(Compile, packageBin) := entryPoint

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.kafka" % "kafka-clients" % "2.6.0",
  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "br.com.zap",
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
  case PathList("module-info.class") => MergeStrategy.discard
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