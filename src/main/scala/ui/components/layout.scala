package app.paperhands.components

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.extra.Ajax
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.model._
import app.paperhands.net._

import scala.scalajs.js

object Layout {
  def apply(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      children: VdomNode*
  ) =
    <.div(
      Tabs(Tabs.Props(proxy)),
      <.div(children: _*)
    )
}
