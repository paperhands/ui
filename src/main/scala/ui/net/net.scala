package app.paperhands.net

import japgolly.scalajs.react.extra.Ajax
import app.paperhands.config.Config

object Net {
  private def pref(p: String) =
    Config.api.prefixPath(p)

  private def get(p: String) =
    Ajax("GET", pref(p)).setRequestContentTypeJsonUtf8

  def getTrending(period: String) =
    get(s"api/v1/quote/trending/$period").send("")

  def getDetails(symbol: String, period: String) =
    get(s"api/v1/quote/details/$symbol/$period").send("")

  def getSamples(symbol: String) =
    get(s"api/v1/content/sample/$symbol").send("")
}
