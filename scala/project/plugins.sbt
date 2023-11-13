addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.14"

// plugin for publishing to gitlab
addSbtPlugin("nl.zolotko.sbt" % "sbt-gitlab" % "0.0.9")
