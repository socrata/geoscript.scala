package org.geoscript.geocss

import org.scalatest.funsuite.AnyFunSuite, org.scalatest.matchers.should.Matchers
import org.geoscript.geocss.compat.CSS2SLD

class JavaCompatibilityTest extends AnyFunSuite with Matchers { 
  test("Java compatibility layer can convert CSS to Style") {
    val stream = getClass.getResourceAsStream("/minimal.css")
    val reader = new java.io.InputStreamReader(stream)
    val style = CSS2SLD.convert(reader)
    style should not be (null)
  }
}
