import sbt._
import sbt.Keys._
import sys.process._
import language.postfixOps

object Slides {
  lazy val slidesSourceFiles = TaskKey[Seq[File]]("slides markdown files")
  lazy val slidesTargetDirectory = SettingKey[File]("slides output directory")
  lazy val slidesRevealjsTheme = SettingKey[String]("slides reveal.js theme")
  lazy val slidesHtml =
    TaskKey[Seq[File]]("slideshtml", "Produce slides in html format")
  lazy val slidesSettings = Seq(
    slidesTargetDirectory := target.value / "slides",
    slidesRevealjsTheme := "white",
    slidesHtml := {
      slidesTargetDirectory.value.mkdirs()
      streams.value.log.info(s"Writing html slides")
      val outputs = slidesSourceFiles.value.map {
        source =>
          val (base, ext) = IO.split(source.getName)
          val htmlName = s"$base.html"
          val outputFile = slidesTargetDirectory.value / htmlName
          s"pandoc --variable=theme:${slidesRevealjsTheme.value} --variable=revealjs-url:lib/revealjs --slide-level=2 --standalone -t revealjs -o $outputFile $source" !;
          outputFile
      }
      streams.value.log.success(s"Wrote ${outputs.length} slides files")
      outputs
    }
  )
}
