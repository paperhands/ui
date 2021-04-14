package app.paperhands.net

import app.paperhands.config.Config
import japgolly.scalajs.react.extra.Ajax

object Net {
  private def pref(p: String) =
    Config.api.prefixPath(p)

  private def get(p: String) =
    Ajax("GET", pref(p)).setRequestContentTypeJsonUtf8

  private def put(p: String) =
    Ajax("PUT", pref(p)).setRequestContentTypeJsonUtf8

  def getTrending(period: String) =
    get(s"api/v1/quote/trending/$period").send("")

  def searchQuotes(term: String) =
    get(s"api/v1/quote/search/$term").send("")

  def getDetails(symbol: String, period: String) =
    get(s"api/v1/quote/details/$symbol/$period").send("")

  def getSamples(symbol: String) =
    get(s"api/v1/content/samples/$symbol").send("")

  def getUnlabeled(limit: Int) =
    get(s"api/v1/content/unlabeled/$limit").send("")

  def labelContent(contentID: String, label: Int) =
    put(s"api/v1/content/label/$contentID/$label").send("")
}
