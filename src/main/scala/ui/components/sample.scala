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
      shouldLabel: Boolean,
      symbol: Option[String]
  )

  case class State(samples: List[Content], labeledIDs: List[String])

  class Backend($ : BackendScope[Props, State]) {
    def setSamples(body: String): Callback = {
      val samples = Model.as[js.Array[Content]](body)
      $.modState(_.copy(samples = samples.toList))
    }

    def loadData(shouldLabel: Boolean, symbol: Option[String]): Callback =
      (if (shouldLabel) Net.getUnlabeled(10)
       else Net.getSamples(symbol.get)).onComplete { xhr =>
        stopLoading >> setSamples(xhr.responseText)
      }.asCallback

    def putLabel(contentID: String, label: Int): Callback =
      Net.labelContent(contentID, label).asCallback >>
        $.modState(s => s.copy(labeledIDs = contentID :: s.labeledIDs))

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
        $.props.flatMap((props: Props) =>
          loadData(props.shouldLabel, props.symbol)
        )

    def mounted: Callback =
      reloadSamples

    def renderSentiment(s: Int) =
      s match {
        case 1 => "🐂"
        case 2 => "🐻"
        case _ => ""
      }

    def labelButtons(contentID: String) =
      <.div(
        <.button(
          ^.onClick --> putLabel(contentID, 0),
          ^.className := "button is-warning mr-3",
          "😐"
        ),
        <.button(
          ^.onClick --> putLabel(contentID, 1),
          ^.className := "button is-success mr-3",
          "🐂"
        ),
        <.button(
          ^.onClick --> putLabel(contentID, 2),
          ^.className := "button is-danger mr-3",
          "🐻"
        ),
        <.button(
          ^.onClick --> putLabel(contentID, -1),
          ^.className := "button",
          "?"
        )
      )

    def renderContent(shouldLabel: Boolean, content: Content) =
      <.div(
        ^.className := "box",
        <.div(
          ^.className := "content",
          <.p(<.strong(content.author), " ", content.createdTime),
          <.p(content.body),
          <.p(<.strong("Symbols: "), content.parsed.symbols.mkString(", "))
            .when(!content.parsed.symbols.isEmpty),
          labelButtons(content.id).when(shouldLabel),
          <.p(
            <.strong("Sentiment: "),
            renderSentiment(content.parsed.sentiment)
          )
            .when(content.parsed.sentiment > 0 && !shouldLabel)
        )
      )

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        <.div(
          ^.className := "block",
          props.symbol.map { symbol =>
            <.button(
              ^.onClick --> ctl.set(AppRouter.Details(symbol)),
              ^.className := "button is-info",
              <.i(^.className := "fas fa-arrow-left")
            )
          },
          <.button(
            ^.onClick --> ctl.set(AppRouter.Index),
            ^.className := "button is-info",
            <.i(^.className := "fas fa-arrow-left")
          ).when(props.shouldLabel),
          <.button(
            ^.onClick --> reloadSamples,
            ^.className := "button is-warning ml-3",
            <.i(^.className := "fas fa-sync")
          )
        ),
        Loading.Content(state.samples.isEmpty),
        <.div(
          state.samples
            .filter((c: Content) => !state.labeledIDs.contains(c.id))
            .map(renderContent(props.shouldLabel, _)): _*
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(List(), List()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
