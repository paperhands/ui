package app.paperhands.components

import org.scalajs.dom.raw.HTMLElement

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.echarts._
import app.paperhands.model._
import app.paperhands.net._

import scala.scalajs.js

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
      Net
        .getDetails(symbol, "1day")
        .onComplete { xhr =>
          stopLoading >> Callback.log(xhr) >> setDetails(xhr.responseText)
        }
        .asCallback

    def setLoading(v: Boolean) =
      $.modState(_.copy(loading = v))

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def mounted: Callback =
      Callback.log("Mounted index") >>
        startLoading >>
        ($.props.map(_.symbol) >>= loadDetails)

    def formatPopularity(details: Details) = {
      val symbol = details.popularity.symbol
      val engagements = details.popularity.engagements
      val mentions = details.popularity.mentions

      <.span(
        s"$symbol was mentioned $mentions and had $engagements comments in conversation around it"
      )
    }

    def optsFromTimeseries(ts: Timeseries) = {
      ChartOptions(
        AxisOptions(
          "category",
          ts.titles
        ),
        AxisOptions("value", js.Array()),
        List(
          Series("line", ts.data)
        )
      )
    }

    def chartFromTimeseries(ts: Timeseries) =
      Chart(Chart.Props(optsFromTimeseries(ts)))

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        ctl.link(AppRouter.Index)("got to index"),
        <.span("LOADING").when(state.loading),
        <.h1(
          s"Details for ${props.symbol} for ${props.interval}"
        ),
        state.details.map(formatPopularity),
        state.details.map(_.engagements).map(chartFromTimeseries),
        state.details.map(_.mentions).map(chartFromTimeseries),
        state.details.map(_.sentiments).map(chartFromTimeseries),
        state.details.map(_.price).map(chartFromTimeseries)
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, None))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
