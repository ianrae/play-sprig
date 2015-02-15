name := "play-sprig"

//0.2 is java8 build
version := "0.2-SNAPSHOT"

scalaVersion := "2.11.4"


libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "commons-io" % "commons-io" % "2.3"
)     

//play.Project.playJavaSettings
lazy val root = (project in file(".")).enablePlugins(PlayJava)
