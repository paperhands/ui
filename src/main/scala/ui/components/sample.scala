package app.paperhands.components


import app.paperhands.diode._
import app.paperhands.model._
import app.paperhands.net._
import app.paperhands.router.AppRouter
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js


object SamplesPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      symbol: String
  )

  case class State(samples: List[Content])

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
      $.props >>= { props =>
        AppDispatch.setLoading(props.proxy, v)
      }

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def reloadSamples =
      startLoading >>
        ($.props.map(_.symbol) >>= loadSamples)

    def mounted: Callback =
      reloadSamples

    def renderSentiment(s: Int) =
      s match {
        case 1 => "🐂"
        case 2 => "🐻"
        case _ => ""
      }

    def renderContent(content: Content) =
      <.div(
        ^.className := "box",
        <.div(
          ^.className := "content",
          <.p(<.strong(content.author), " ", content.createdTime),
          <.p(content.body),
          <.p(<.strong("Symbols: "), content.parsed.symbols.mkString(", "))
            .when(!content.parsed.symbols.isEmpty),
          <.p(
            <.strong("Sentiment: "),
            renderSentiment(content.parsed.sentiment)
          )
            .when(content.parsed.sentiment > 0)
        )
      )

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        <.div(
          ^.className := "block",
          <.button(
            ^.onClick --> ctl.set(AppRouter.Details(props.symbol)),
            ^.className := "button is-info",
            <.i(^.className := "fas fa-arrow-left")
          ),
          <.button(
            ^.onClick --> reloadSamples,
            ^.className := "button is-warning ml-3",
            <.i(^.className := "fas fa-sync")
          )
        ),
        Loading.Content(state.samples.isEmpty),
        <.div(
          state.samples.map(renderContent): _*
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(List()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
