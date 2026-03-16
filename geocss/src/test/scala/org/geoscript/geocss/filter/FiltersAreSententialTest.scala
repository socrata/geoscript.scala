package org.geoscript.geocss.filter

import org.geoscript.support.interval.Interval
import org.opengis.filter.Filter
import org.geotools.filter.text.ecql.ECQL.toFilter

import org.scalatest.funsuite.AnyFunSuite, org.scalatest.matchers.should.Matchers

class FiltersAreSententialTest extends AnyFunSuite with Matchers {
  import FiltersAreSentential._

  test("Null") {
    constraint(toFilter("A IS NULL")) should equal(IsNull("A"))
  }

  test("Not Null") {
    constraint(toFilter("A IS NOT NULL")) should equal(In("A", Interval.Full))
  }

  test("Not Equals") {
    constraint(toFilter("A <> 1")) should equal(IsNot("A", Value("1")))
  }

  test("Disproves") {
    disprovenBy(Set(toFilter("A <> 1")), toFilter("A IS NULL")) should equal(true)
  }
}
