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
      symbol: String
  )

  case class State(loading: Boolean, details: Option[Details])

  class Backend($ : BackendScope[Props, State]) {
    def formatDatetimeString(s: String, period: String) =
      Format.formatDateFor(Parse.dateFromString(s), period)

    def formatTimeseries(ts: Timeseries, period: String): Timeseries =
      js.Dynamic
        .literal(
          titles = ts.titles.map(formatDatetimeString(_, period)),
          data = ts.data
        )
        .asInstanceOf[Timeseries]

    // TODO this is ugly, think about a better way
    def formatDetails(details: Details, period: String): Details =
      js.Dynamic
        .literal(
          mentions = formatTimeseries(details.mentions, period),
          engagements = formatTimeseries(details.engagements, period),
          sentiments = formatTimeseries(details.sentiments, period),
          price = formatTimeseries(details.price, period),
          popularity = details.popularity
        )
        .asInstanceOf[Details]

    def setDetails(body: String, period: String): Callback = {
      val details = formatDetails(Model.as[Details](body), period)
      $.modState(_.copy(details = Some(details)))
    }

    def loadDetails(symbol: String): Callback =
      $.props >>= { props =>
        val proxy = props.proxy()
        val period = proxy.currentPeriod

        Net
          .getDetails(symbol, period)
          .onComplete { xhr =>
            stopLoading >> setDetails(xhr.responseText, period)
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
          js.Array("Engagement", "Mentions"),
          data.engagements,
          series
        )
      chrartFromOpts(opts)
    }

    def sentimentChart(data: Details): VdomElement = {
      val ts = data.sentiments

      val opts = ChartOpts.optsFromTimeseriesAndSeries(
        js.Array("Sentiment"),
        ts,
        ChartOpts.sentimentSeries(ts)
      )

      chrartFromOpts(opts)
    }

    def title(t: String) =
      <.h1(^.className := "title", t)

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
          )
        ),
        state.details
          .map { details =>
            <.div(
              formatPopularity(details),
              title("Engagement & Mentions"),
              engagementAndMentionChart(details),
              title("Sentiment"),
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
