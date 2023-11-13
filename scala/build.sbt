ThisBuild / version          := Settings.getVersion
ThisBuild / organization     := "shikai.ng"
ThisBuild / organizationName := "test"

lazy val root = (project in file(".")).settings(
  name := "scala-proto",
  Settings.allSettings
)
