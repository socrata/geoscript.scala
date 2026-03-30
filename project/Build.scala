import sbt._
import Keys._
import scala.sys.process._

object GeoScript {
  lazy val gtVersion =
    settingKey[String]("Version number for GeoTools modules")

  val meta =
    Seq[Setting[_]](
      organization := "com.socrata",
      version := "0.8.5",
      scalaVersion := "2.12.21",
      scalacOptions ++= Seq("-feature", "-deprecation", "-Xlint", "-unchecked"),
      javacOptions ++= Seq("--release", "25"),
      publishTo := Some(Resolver.file("file", file("release")))
    )

  val common =
    Seq[Setting[_]](
      fork := true,
      resolvers ++= Seq(
        "osgeo" at "https://repo.osgeo.org/repository/release/",
        "socrata artifactory" at "https://repo.socrata.com/artifactory/libs-release"
      )
    ) ++ meta

  val sphinxSettings =
    Seq(
      baseDirectory := thisProject.value.base,
      target := baseDirectory.value / "target",
      sphinxDir := crossTarget.value / "sphinx",
      sphinxSource := baseDirectory.value / "src" / "main" / "sphinx",
      sphinxBuild := "sphinx-build",
      sphinxOpts := Nil,
      sphinx := runSphinx(sphinxBuild.value, sphinxSource.value, sphinxDir.value, sphinxOpts.value),
      watchSources ++= {
        val b = baseDirectory.value
        val t = target.value
        ((b ** "*") --- (t ** "*")).get
      }
    )

  lazy val root =
    project
      .in(file("."))
      .settings(common)
      .settings(Test / fork := false, publish / skip := true)
      .aggregate(css, examples, library)

  lazy val css =
    project
      .in(file("geocss"))
      .settings(common)
      .settings(gtVersion := "27.5")

  lazy val examples =
    project
      .in(file("examples"))
      .settings(common)
      .settings(Test / fork := false, publish / skip := true)
      .dependsOn(library)

  lazy val library =
    project
      .in(file("geoscript"))
      .settings(common)
      .settings(gtVersion := "27.5")
      .settings(sphinxSettings)
      .dependsOn(css)

  lazy val sphinx =
    taskKey[java.io.File]("runs sphinx documentation generator")
  lazy val sphinxBuild =
    settingKey[String]("command to use when building sphinx")
  lazy val sphinxOpts =
    settingKey[Seq[String]]("options to pass to sphinx-build script")
  lazy val sphinxSource =
    settingKey[java.io.File]("source directory for sphinx docs")
  lazy val sphinxDir =
    settingKey[java.io.File]("output directory for sphinx docs")

  def runSphinx(script: String, input: java.io.File, output: java.io.File, opts: Seq[String]) = {
    val cmd = Seq(script) ++ opts ++ Seq("-b", "html", "-d",
      (output / "doctrees").getAbsolutePath,
      input.getAbsolutePath,
      (output / "html").getAbsolutePath)
    cmd.!
    output / "html"
  }
}
