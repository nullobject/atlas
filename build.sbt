scalaVersion := "2.10.0-RC5"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked")

resolvers ++= Seq(
  "Spray Repository" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0-RC6" cross CrossVersion.full,
  "com.typesafe.akka" %% "akka-agent" % "2.1.0-RC6" cross CrossVersion.full,
  "com.typesafe.akka" %% "akka-testkit" % "2.1.0-RC6" % "test" cross CrossVersion.full,
  "io.spray" % "spray-can" % "1.1-M7",
  "io.spray" % "spray-httpx" % "1.1-M7",
  "io.spray" %% "spray-json" % "1.2.3" cross CrossVersion.full,
  "io.spray" % "spray-routing" % "1.1-M7",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0-M6" % "test" cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "2.0.M5-B1" % "test" cross CrossVersion.full,
  "org.scalaz" %% "scalaz-core" % "7.0.0-M6" cross CrossVersion.full
)

initialCommands in console := "import scalaz._, Scalaz._"
