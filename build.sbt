name:= "scalaista"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.3"

organization := "com.newgenco.scalas"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % Test
)
