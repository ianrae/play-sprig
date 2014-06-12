name := "sprig"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "commons-io" % "commons-io" % "2.3"
)     

play.Project.playJavaSettings
