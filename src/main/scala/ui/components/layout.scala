package app.paperhands.components

import app.paperhands.diode._
import app.paperhands.router.AppRouter
import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

object Layout {
  def apply(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      children: VdomNode*
  ) =
    <.div(
      Loading.Modal(proxy),
      Tabs(Tabs.Props(proxy, ctl)),
      <.div(children: _*)
    )
}
