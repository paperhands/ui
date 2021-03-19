package app.paperhands.components

import org.scalajs.dom.raw.HTMLElement

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._

import scala.scalajs.js

import typings.echarts.echartsRequire
import typings.echarts.mod.init
import typings.echarts.echarts._
import typings.echarts.echarts.EChartOption._
import typings.std.HTMLDivElement

object Chart {
  case class Props(
      opts: EChartOption[Series]
  )

  object Props {
    def fromTimeSeries() = {}
  }

  case class State(echart: Option[ECharts])

  class Backend($ : BackendScope[Props, State]) {
    def setChart(c: ECharts): Callback =
      $.modState(_.copy(echart = Some(c)))

    def initChart(p: Props): Callback =
      $.getDOMNode.map { dom =>
        val el = dom.toElement.get.asInstanceOf[HTMLDivElement]
        val chart = init(el)
        chart.setOption(p.opts)
        chart
      } >>= setChart

    def mounted: Callback =
      Callback.log("Mounted chart") >>
        ($.props >>= initChart)

    def update(np: Props): Callback = {
      Callback.log("Got new props") >>
        $.state.map { state =>
          println(s"checking state $state")
          state.echart.foreach(_.setOption(np.opts))
        }
    }

    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "chart-target"
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(None))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .componentWillReceiveProps(scope => scope.backend.update(scope.nextProps))
    .build

  def apply(props: Props) = Component(props)
}
