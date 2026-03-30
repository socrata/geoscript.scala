import GeoScript.gtVersion

name := "geocss"

libraryDependencies ++= {
  val v = gtVersion.value
  Seq(
    "org.geotools" % "gt-main" % v,
    "org.geotools" % "gt-cql" % v,
    "org.geotools" % "gt-xml" % v
  )
}

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
  "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "org.scalatestplus" %% "scalacheck-1-19" % "3.2.19.0" % "test"
)

initialCommands += """
import org.{ geotools => gt }
import org.opengis.{ filter => ogc }
import org.geoscript._
import gt.filter.text.ecql.ECQL.{ toFilter => cql }
import geocss.filter.FiltersAreSentential
import support.logic.{ given, reduce }
def in(path: String) = new java.io.FileReader(new java.io.File(path))
def load(path: String) =
  geocss.CssParser.parseAll(
    geocss.CssParser.styleSheet, in(path)
  ).get
val tx = new geocss.Translator()
"""
