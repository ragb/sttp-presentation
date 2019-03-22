import sbt._

object Dependencies {

  lazy val sttpCore = "com.softwaremill.sttp" %% "core" % sttpVersion
  lazy val sttpCirce = "com.softwaremill.sttp" %% "circe" % sttpVersion

  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeGenericExtras = "io.circe" %% "circe-generic-extras" % circeVersion
  lazy val catsCore = "org.typelevel" %% "cats-core" % catsVersion

  lazy val specs2Core = "org.specs2" %% "specs2-core" % specs2Version
  lazy val specs2Scalacheck = "org.specs2" %% "specs2-scalacheck" % specs2Version
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckVersion

  val sttpVersion = "1.5.11"
  val circeVersion = "0.11.1"
  val catsVersion = "1.6.0"
  val specs2Version = "4.5.1"
  val scalacheckVersion = "1.14.0"
  val kindProjectorVersion = "0.9.7"
}
