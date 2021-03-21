package app.paperhands.components

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

object SamplesPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      symbol: String
  )

  case class State(loading: Boolean, samples: List[Content])

  class Backend($ : BackendScope[Props, State]) {
    def setSamples(body: String): Callback = {
      val samples = Model.as[js.Array[Content]](body)
      $.modState(_.copy(samples = samples.toList))
    }

    def loadSamples(symbol: String): Callback =
      Net
        .getSamples(symbol)
        .onComplete { xhr =>
          stopLoading >> setSamples(xhr.responseText)
        }
        .asCallback

    def setLoading(v: Boolean) =
      $.modState(_.copy(loading = v))

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def reloadSamples =
      startLoading >>
        ($.props.map(_.symbol) >>= loadSamples)

    def mounted: Callback =
      reloadSamples

    def renderContent(content: Content) =
      <.div(
        ^.className := "box",
        <.div(
          ^.className := "content",
          <.p(<.strong(content.author), " ", content.createdTime),
          <.p(content.body),
          <.p(<.strong("Symbols: "), content.parsed.symbols.mkString(", "))
            .when(content.parsed.symbols.length > 0),
          <.p(<.strong("Sentiment: "), content.parsed.sentiment)
            .when(content.parsed.sentiment > 0)
        )
      )

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        Loading.Modal(state.loading),
        Tabs(Tabs.Props(props.proxy)),
        <.div(
          ^.className := "block",
          <.button(
            ^.onClick --> ctl.set(AppRouter.Index),
            ^.className := "button is-info",
            <.i(^.className := "fas fa-arrow-left")
          ),
          <.button(
            ^.onClick --> reloadSamples,
            ^.className := "button is-warning ml-3",
            <.i(^.className := "fas fa-sync")
          )
        ),
        <.div(
          state.samples.map(renderContent): _*
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, List()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
