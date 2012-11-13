resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq(
  "io.spray" %% "spray-json" % "1.2.2" cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "2.0.M5" % "test"
)
