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

object Tabs {
  case class Props(
      proxy: ModelProxy[AppState]
  )

  case class State()

  class Backend($ : BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      val proxy = props.proxy()
      val current = proxy.currentInternal
      val ranges = List("1D", "5D", "1W", "1M", "6M", "1Y")

      <.div(
        ^.className := "tabs is-centered",
        <.ul(
          ranges.map { interval =>
            val klass = if (current == interval) "is-active" else ""

            <.li(
              ^.onClick --> props.proxy.dispatchCB(SetInterval(interval)),
              ^.className := klass,
              <.a(<.span(interval))
            )
          }: _*
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(props: Props) = Component(props)
}
