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
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State()

  def tabs(proxy: ModelProxy[AppState]) = {
    val appState = proxy()
    val current = appState.currentPeriod
    val ranges = AppConnstants.viewRanges

    <.div(
      ^.className := "tabs is-centered",
      <.ul(
        ranges.map { interval =>
          val klass = if (current == interval) "is-active" else ""

          <.li(
            ^.onClick --> proxy.dispatchCB(SetInterval(interval)),
            ^.className := klass,
            <.a(<.span(interval))
          )
        }: _*
      )
    )
  }

  val autoRefresh =
    ScalaComponent
      .builder[ModelProxy[AppState]]
      .initialState(false)
      .render { $ =>
        val ranges = AppConnstants.refreshIntervals
        val proxy = $.props
        val isActive = $.state
        val k = if (isActive) "is-active" else ""
        val appState = proxy()
        val label = appState.autoRefresh.getOrElse("Auto Refresh")
        val currentValue = appState.autoRefresh.getOrElse("off")

        <.div(
          ^.className := s"dropdown $k",
          <.div(
            ^.className := "dropdown-trigger",
            <.button(
              ^.className := "button",
              ^.aria.haspopup.`true`,
              ^.aria.controls := "autorefresh-menu",
              ^.onClick --> $.modState(!_),
              <.span(label),
              <.span(
                ^.className := "icon is-small",
                <.i(
                  ^.className := "fas fa-angle-down",
                  ^.aria.hidden := true
                )
              )
            )
          ),
          <.div(
            ^.className := "dropdown-menu",
            ^.role.menu,
            ^.id := "autorefresh-menu",
            <.div(
              ^.className := "dropdown-content",
              <.div(
                ^.className := "dropdown-item",
                <.p("Change auto-refresh interval.")
              ),
              <.hr(^.className := "dropdown-divider"),
              <.div(
                ranges.map { range =>
                  val msg = range match {
                    case "off" => None
                    case v     => Some(v)
                  }

                  val cb = proxy.dispatchCB(
                    SetAutoRefresh(msg)
                  ) >> $.setState(false)

                  val k = if (currentValue == range) "is-active" else ""

                  <.a(
                    ^.className := s"dropdown-item $k",
                    ^.onClick --> cb,
                    range
                  )
                }: _*
              )
            )
          )
        )
      }
      .build

  class Backend($ : BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      <.div(
        tabs(props.proxy),
        <.div(
          ^.className := "columns mb-3",
          <.div(
            ^.className := "column",
            autoRefresh(props.proxy)
          ),
          <.div(
            ^.className := "column",
            <.div(
              ^.className := "is-pulled-right",
              Search(Search.Props(props.proxy, props.ctl))
            )
          )
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
