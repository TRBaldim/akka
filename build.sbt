name := "akka"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.10",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.10" % Test
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test