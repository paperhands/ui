package app.paperhands.net

import japgolly.scalajs.react.extra.Ajax
import app.paperhands.config.Config

object Net {
  private def pref(p: String) =
    Config.api.prefixPath(p)

  private def get(p: String) =
    Ajax("GET", pref(p)).setRequestContentTypeJsonUtf8

  def getTrending =
    get("api/v1/quote/trending").send("")
}
