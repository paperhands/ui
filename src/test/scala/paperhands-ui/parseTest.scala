package app.paperhands.tests

import minitest._

import app.paperhands.model.Parse

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
}
