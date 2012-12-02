scalaVersion := "2.10.0-RC3"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked")

resolvers ++= Seq(
  "Spray Repository" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0-RC3" cross CrossVersion.full,
  "com.typesafe.akka" %% "akka-testkit" % "2.1.0-RC3" % "test" cross CrossVersion.full,
  "com.typesafe.akka" %% "akka-zeromq" % "2.1.0-RC3" cross CrossVersion.full,
  "io.spray" %% "spray-json" % "1.2.3" cross CrossVersion.full,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0-M6" % "test" cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "2.0.M5-B1" % "test" cross CrossVersion.full
)
