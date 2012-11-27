scalacOptions ++= Seq("-Ydependent-method-types", "-deprecation", "-unchecked")

resolvers ++= Seq(
  "Spray Repository" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0.4",
  "com.typesafe.akka" % "akka-testkit" % "2.0.4" % "test",
  "io.spray" %% "spray-json" % "1.2.2" cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "1.8" % "test"
)
