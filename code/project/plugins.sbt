// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.0")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")

// Play Ebean support, to enable, uncomment this line, and enable in your build.sbt using
// enablePlugins(SbtEbean). Note, uncommenting this line will automatically bring in
// Play enhancer, regardless of whether the line above is commented out or not.
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "1.0.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0-RC2")

