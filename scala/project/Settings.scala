import sbt.Keys._
import sbt.librarymanagement.Configurations.Compile
import sbt.{Def, io, _}
import sbtprotoc.ProtocPlugin.autoImport.PB

import scala.collection.immutable

object Settings {

  lazy val scala212 = "2.12.12"
  lazy val supportedScalaVersions: immutable.Seq[String] = List(scala212)
  val protoSources: Seq[sbt.File] = Seq(
    file("../protos/")
  )

  val pbGenSettings: Seq[Def.Setting[_]] = Seq(
    Compile / unmanagedResourceDirectories ++= protoSources, // to include the raw proto files in the jar
    Compile / PB.protoSources := protoSources,
    Compile / PB.targets := Seq(
      PB.gens.java -> (Compile / sourceManaged).value,
      scalapb.gen(javaConversions = true, lenses = true) -> (Compile / sourceManaged).value
    ),
    // ScalaPB generate scala sources which then need to be included in the source jar (useful for local development)
    Compile / packageSrc / mappings ++= {
      val allGeneratedFiles = ((Compile / sourceManaged).value ** "*").filter(_.isFile)
      allGeneratedFiles.get.pair(io.Path.relativeTo((Compile / sourceManaged).value))
    }
  )

  val dependencies: Seq[ModuleID] = Seq(
    compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.1" cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % "1.7.1" % Provided cross CrossVersion.full,
    "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
  )

  val allSettings: Seq[Def.Setting[_]] = pbGenSettings ++ Seq(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions += "-P:silencer:pathFilters=src_managed",
    crossScalaVersions := supportedScalaVersions,
    scalaVersion := scala212,
    libraryDependencies ++= dependencies
  )

  def getVersion: String = {
    // Some branch names can contain slashes which will break the name
    val branchName = sys.env.get("CI_COMMIT_REF_NAME").map(_.replace("/", "-"))
    val jobID = sys.env.get("CI_JOB_ID")

    if (branchName.isEmpty || jobID.isEmpty) "local"
    else s"${branchName.get}-${jobID.get}"
  }
}
