name := "plongo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0",
  "org.mongodb" % "mongo-java-driver" % "2.12.2",
  "ro.fortsoft.ff2j" % "ff2j" % "0.6",
  cache
)

play.Project.playJavaSettings
