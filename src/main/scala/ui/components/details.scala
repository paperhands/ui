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

object DetailsPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      symbol: String
  )

  case class State(loading: Boolean, details: Option[Details])

  class Backend($ : BackendScope[Props, State]) {
    def fmtDatetimeString(s: String, period: String) =
      Format.fmtDateFor(Parse.dateFromString(s), period)

    def fmtTimeseries(ts: Timeseries, period: String): Timeseries =
      TimeseriesLens.titles.modify(_.map(fmtDatetimeString(_, period)))(ts)

    def modifyDetailsLens(period: String) =
      List(
        DetailsLens.mentions,
        DetailsLens.engagements,
        DetailsLens.sentiments,
        DetailsLens.price
      )
        .map(_.modify(fmtTimeseries(_, period)))
        .reduce(_ compose _)

    def fmtDetails(details: Details, period: String): Details =
      modifyDetailsLens(period)(details)

    def setDetails(body: String, period: String): Callback = {
      val details = fmtDetails(Model.as[Details](body), period)
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

    def fmtPopularity(details: Details) = {
      val currentPrice = details.currentPrice
      val symbol = details.popularity.symbol
      val engagements = details.popularity.engagements
      val mentions = details.popularity.mentions

      <.section(
        ^.className := "hero is-link mb-6",
        <.div(
          ^.className := "hero-body",
          <.p(
            ^.className := "title",
            s"$$$symbol $currentPrice USD"
          ),
          <.div(
            ^.className := "subtitle",
            <.p(details.desc),
            <.p(
              ^.className := "mt-3",
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
              " comments in conversation around this symbol"
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
              fmtPopularity(details),
              <.h1(
                ^.className := "title",
                <.abbr(
                  ^.title := "Volume of comments engaged in conversation around this stock",
                  "Engagement"
                ),
                " & ",
                <.abbr(
                  ^.title := "Volume of comments that directly mention this stock",
                  "Mentions"
                )
              ),
              engagementAndMentionChart(details),
              <.h1(
                ^.className := "title",
                <.abbr(
                  ^.title := "Sum of bull and bear sentiments from comments that directly mention this stock (positive value indicates bullishness, negative value indicates bearishness)",
                  "Sentiment"
                )
              ),
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
