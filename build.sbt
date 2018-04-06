name := "akka"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "1.1.0",
  "org.apache.kafka" % "kafka-streams" % "1.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.10",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.10" % Test,
  "com.typesafe.akka" %% "akka-persistence" % "2.5.11",
  "com.typesafe.akka" %% "akka-remote" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster-metrics" % "2.5.11",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni" % "1.8"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test