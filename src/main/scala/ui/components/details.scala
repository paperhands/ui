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

import scala.scalajs.js
import scala.scalajs.js.`|`

import typings.zrender.zrender.LinearGradient // not used for now, area style only expects string as color atm
import typings.echarts.echarts._
import typings.echarts.echarts.EChartOption._
import typings.echarts.echarts.EChartOption.BasicComponents.CartesianAxis.DataObject
import typings.echarts.echarts.EChartOption.BasicComponents.CartesianAxis.Type
import typings.echarts.anon
import typings.echarts.anon.ShadowBlur
import typings.echarts.anon.BorderRadius
import typings.echarts.anon.ColorOpacity
import typings.echarts.mod.graphic
import typings.echarts.echartsStrings
import typings.echarts.echarts.EChartOption.BasicComponents.CartesianAxis.PointerLabel

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

    def optsFromTimeseries(
        legendLabel: String
    )(ts: Timeseries): EChartOption[Series] = {
      val as = ColorOpacity().setOpacity(0.2).setColor("rgba(128, 255, 165)")

      val sb = ShadowBlur().setWidth(0)
      val label = BorderRadius().setShow(false).setPosition("top")
      val series = SeriesLine_()
        .setType("line")
        .setData(ts.data.map(_.toDouble))
        .setSmooth(true)
        .setLineStyle(sb)
        .setLabel(label)
        .setAreaStyle(as)
        .setShowSymbol(false)
        .setEmphasis(
          js.Dynamic.literal("focus" -> "series").asInstanceOf[anon.`8`]
        )

      val xAxis = XAxis()
        .setType(Type.category)
        .setData(ts.titles)
      val yAxis = YAxis()
        .setType(Type.value)
        .setData(js.Array())

      val tb = js.Dynamic.literal(
        "feature" -> js.Dynamic.literal(
          "saveAsImage" -> js.Object()
        )
      )

      val grid = Grid()
        .setContainLabel(true)
        .setLeft("3%")
        .setRight("4%")
        .setBottom("3%")

      val legend = Legend_()
        .setData(js.Array(legendLabel))

      val pl = PointerLabel().setBackgroundColor("#6a7985")
      val ap = Tooltip
        .AxisPointer()
        .setType(echartsStrings.line)
        .setLabel(pl)
      val tooltip = Tooltip_()
        .setTrigger(echartsStrings.axis)
        .setAxisPointer(ap)

      val opts = EChartOption_[Series]()
        .setXAxis(xAxis)
        .setYAxis(yAxis)
        .setSeries(js.Array(series))
        .setToolbox(tb)
        .setGrid(grid)
        .setLegend(legend)
        .setTooltip(tooltip)

      opts
    }

    def chrartFromOpts(opts: EChartOption[Series]) =
      Chart(Chart.Props(opts))

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        ctl.link(AppRouter.Index)("got to index"),
        <.span("LOADING").when(state.loading),
        <.h1(
          s"Details for ${props.symbol} for ${props.interval}"
        ),
        state.details.map(formatPopularity),
        state.details
          .map(_.engagements)
          .map(optsFromTimeseries("Engagement"))
          .map(chrartFromOpts),
        state.details
          .map(_.mentions)
          .map(optsFromTimeseries("Mentions"))
          .map(chrartFromOpts),
        state.details
          .map(_.sentiments)
          .map(optsFromTimeseries("Sentiment"))
          .map(chrartFromOpts)
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
