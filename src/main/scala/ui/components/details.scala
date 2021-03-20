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

object DetailsPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      symbol: String,
      interval: String
  )

  case class State(loading: Boolean, details: Option[Details])

  class Backend($ : BackendScope[Props, State]) {
    def setDetails(body: String): Callback = {
      val details = Model.as[Details](body)
      $.modState(_.copy(details = Some(details)))
    }

    def loadDetails(symbol: String): Callback =
      $.props >>= { props =>
        val proxy = props.proxy()
        val period = proxy.currentPeriod

        Net
          .getDetails(symbol, period)
          .onComplete { xhr =>
            stopLoading >> setDetails(xhr.responseText)
          }
          .asCallback
      }

    def setLoading(v: Boolean) =
      $.modState(_.copy(loading = v))

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def mounted: Callback =
      startLoading >>
        ($.props.map(_.symbol) >>= loadDetails)

    def willReceiveProps: Callback =
      startLoading >>
        ($.props.map(_.symbol) >>= loadDetails)

    def formatPopularity(details: Details) = {
      val symbol = details.popularity.symbol
      val engagements = details.popularity.engagements
      val mentions = details.popularity.mentions

      <.section(
        ^.className := "hero is-link mb-6",
        <.div(
          ^.className := "hero-body",
          <.p(
            ^.className := "title",
            s"$$$symbol"
          ),
          <.div(
            ^.className := "subtitle",
            <.p(
              "Some numbers:"
            ),
            <.p(
              ^.className := "ml-3",
              <.span(^.className := "has-text-weight-bold", mentions),
              " times mentioned directly"
            ),
            <.p(
              ^.className := "ml-3",
              <.span(^.className := "has-text-weight-bold", engagements),
              " comments involving this symbol"
            )
          )
        )
      )
    }

    def chrartFromOpts(opts: js.Object) =
      <.div(^.className := "block", Chart(Chart.Props(opts)))

    def engagementAndMentionChart(data: Details): VdomElement = {
      val series =
        ChartOpts.engagementMentionsSeries(data.engagements, data.mentions)

      val opts = ChartOpts
        .optsFromTimeseriesAndSeries(
          "Engagement & Mentions",
          js.Array("Engagement", "Mentions"),
          data.engagements,
          series
        )
      chrartFromOpts(opts)
    }

    def sentimentChart(data: Details): VdomElement = {
      val ts = data.sentiments

      val opts = ChartOpts.optsFromTimeseriesAndSeries(
        "Sentiment",
        js.Array("Sentiment"),
        ts,
        ChartOpts.sentimentSeries(ts)
      )

      chrartFromOpts(opts)
    }

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        Loading.Modal().when(state.loading),
        Tabs(Tabs.Props(props.proxy)),
        <.div(
          ^.className := "block",
          <.button(
            ^.onClick --> ctl.set(AppRouter.Index),
            ^.className := "button is-info",
            <.i(^.className := s"fas fa-arrow-left")
          )
        ),
        state.details
          .map { details =>
            <.div(
              formatPopularity(details),
              engagementAndMentionChart(details),
              sentimentChart(details)
            )

          }
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, None))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .componentWillReceiveProps(_.backend.willReceiveProps)
    .build

  def apply(props: Props) = Component(props)
}
