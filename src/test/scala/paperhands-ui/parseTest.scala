package app.paperhands.tests

import minitest._

import app.paperhands.model._

object MySimpleSuite extends SimpleTestSuite {
  test("should parse string to date") {
    val d = Parse.dateFromString("2021-03-19T19:15:00Z")
    assertEquals(d.getUTCFullYear(), 2021)
    assertEquals(d.getUTCMonth(), 2)
    assertEquals(d.getUTCDate(), 19)
    assertEquals(d.getUTCHours(), 19)
    assertEquals(d.getUTCMinutes(), 15)
    assertEquals(d.getUTCSeconds(), 0)
  }

  test("should format date") {
    val d = Parse.dateFromString("2021-03-19T19:15:00Z")

    // FIXME This test is timezone dependent?
    // assertEquals(Format.fmtDateFor(d, "1D"), "15:15")
    assertEquals(Format.fmtDateFor(d, "5D"), "Friday")
    assertEquals(Format.fmtDateFor(d, "1W"), "Friday")
    assertEquals(Format.fmtDateFor(d, "1M"), "Mar. 19")
    assertEquals(Format.fmtDateFor(d, "6M"), "Mar. 19, 21")
    assertEquals(Format.fmtDateFor(d, "1Y"), "Mar. 19, 21")
  }
}
