import Dependencies._

import Slides._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "co.enear",
      scalaVersion := "2.12.8",
      version := "0.1.0-SNAPSHOT",
      organizationName := "e.Near",
      startYear := Some(2019),
      licenses += ("Apache-2.0", new URL(
        "https://www.apache.org/licenses/LICENSE-2.0.txt"))
    )),
    name := "pxlscamp-sttp",
    libraryDependencies ++= Seq(
      sttpCore,
      sttpCirce,
      circeCore,
      circeGenericExtras,
      specs2Core % Test,
      specs2Scalacheck % Test,
      scalacheck % Test
    ),
    addCompilerPlugin(
      "org.spire-math" % "kind-projector" % kindProjectorVersion cross CrossVersion.binary),
    git.useGitDescribe := true
  )
  .settings(slidesSettings: _*)
  .settings(
    slidesSourceFiles := tut.value.map(_._1),
    scalacOptions in Tut --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
    initialCommands in console := """

      import co.enear.presentations._
      import com.softwaremill.sttp._
      import Encoders._, Decoders._
"""
  )
  .enablePlugins(GitVersioning, TutPlugin)
