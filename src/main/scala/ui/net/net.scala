package app.paperhands.net

import app.paperhands.config.Config
import japgolly.scalajs.react.extra.Ajax

object Net {
  private def pref(p: String) =
    Config.api.prefixPath(p)

  private def get(p: String) =
    Ajax("GET", pref(p)).setRequestContentTypeJsonUtf8

  def getTrending(period: String) =
    get(s"api/v1/quote/trending/$period").send("")

  def searchQuotes(term: String) =
    get(s"api/v1/quote/search/$term").send("")

  def getDetails(symbol: String, period: String) =
    get(s"api/v1/quote/details/$symbol/$period").send("")

  def getSamples(symbol: String) =
    get(s"api/v1/content/sample/$symbol").send("")
}
