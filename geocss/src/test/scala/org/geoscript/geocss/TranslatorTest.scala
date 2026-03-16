package org.geoscript.geocss

import org.scalatest._, funsuite.AnyFunSuite, matchers.should._

class TranslatorTest extends AnyFunSuite with Matchers {
  val Translator = new Translator
  import Translator.color
  
  def body(x: Translator.OGCExpression): String =
    x match {
      case x: org.opengis.filter.expression.Literal =>
        x.getValue.toString.toLowerCase
      case _ => sys.error("Expected a Literal but actually got " + x)
    }

  test("Short hexcodes and English names should work as colors") {
    body(color(Literal("#FFAA77"))) should equal("#ffaa77")
    body(color(Function("rgb", List("1.0", "0.665", "0.467") map Literal))) should equal("#ffaa77")
    body(color(Literal("#FA7"))) should equal("#ffaa77")
    body(color(Function("rgb", List("255", "170", "119") map Literal))) should equal("#ffaa77")
  }
}
