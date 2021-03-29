package app.paperhands.components

import scala.concurrent.duration._

import org.scalajs.dom.raw.HTMLElement

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.model._
import app.paperhands.net._
import app.paperhands.chart._

import scala.scalajs.js
import scala.scalajs.js.`|`

import monocle.macros.syntax.all._

object Search {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State(
      loading: Boolean,
      results: List[Quote],
      term: String,
      active: Boolean
  )

  class Backend($ : BackendScope[Props, State]) {
    def resetTerm =
      $.modState(_.copy(term = ""))

    def resetResults =
      $.modState(_.copy(results = List()))

    def setSearch(term: String, body: String): Callback = {
      val results = Model.as[js.Array[Quote]](body)
      $.modState(s =>
        if (s.term == term) s.copy(results = results.toList) else s
      )
    }

    def loadSearch(term: String): Callback =
      Net
        .searchQuotes(term)
        .onComplete { xhr =>
          setSearch(term, xhr.responseText) >> stopLoading
        }
        .asCallback

    def setLoading(v: Boolean) =
      $.modState(_.copy(loading = v))

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def setTerm(term: String) =
      $.modState(_.copy(term = term))

    def search(term: String) =
      if (term.length > 1)
        (startLoading >> resetResults >> loadSearch(term))
          .rateLimit(2.second, 1)
          .void
      else
        stopLoading >> resetResults

    def inputChange(e: ReactFormEventFromInput) = {
      val term = e.target.value
      setActive(true) >> setTerm(term) >> search(term)
    }

    def setActive(v: Boolean) =
      $.modState(_.copy(active = v))

    def stopAndReset =
      stopLoading >> resetResults

    def onFocus =
      stopAndReset >> setActive(true)

    def onBlur =
      (stopAndReset >> setActive(false)).debounce(1.second)

    def render(props: Props, state: State): VdomElement = {
      val proxy = props.proxy
      val ctl = props.ctl
      val k =
        if ((state.active && !state.results.isEmpty) || state.loading)
          "is-active"
        else ""

      <.div(
        ^.className := s"dropdown $k",
        <.div(
          ^.className := "dropdown-trigger",
          <.div(
            ^.className := "field",
            <.p(
              ^.className := "control is-expanded has-icons-right",
              <.input(
                ^.className := "input",
                ^.`type` := "search",
                ^.placeholder := "Search...",
                ^.value := state.term,
                ^.onChange ==> inputChange,
                ^.onFocus --> onFocus,
                ^.onBlur --> onBlur
              ),
              <.span(
                ^.className := "icon is-small is-right",
                <.i(^.className := "fas fa-search")
              )
            )
          )
        ),
        <.div(
          ^.className := "dropdown-menu",
          ^.role.menu,
          ^.id := "search-menu",
          <.div(
            ^.className := "dropdown-content",
            <.div(
              ^.className := "dropdown-item",
              <.p("Loading...")
            ).when(state.loading),
            <.div(
              ^.className := "dropdown-item",
              <.p(s"Nothing found for ${state.term}")
            ).when(
              state.active && state.results.isEmpty && !state.loading && state.term.length > 1
            ),
            <.div(
              state.results.map { quote =>
                val s = quote.symbol
                val d = quote.desc
                val cb = resetResults >> resetTerm >> ctl.set(
                  AppRouter.Details(s.toLowerCase)
                )

                <.a(
                  ^.className := "dropdown-item",
                  ^.onClick --> cb,
                  s"$s - $d"
                )
              }: _*
            )
          )
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, List(), "", false))
    .renderBackend[Backend]
    .build

  def apply(props: Props) = Component(props)
}
