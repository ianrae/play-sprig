name := "play-sprig"

//0.2 is play 2.4 build
version := "0.2.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  "commons-io" % "commons-io" % "2.3"
)     

//play.Project.playJavaSettings
lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes 
EclipseKeys.preTasks := Seq(compile in Compile)     
