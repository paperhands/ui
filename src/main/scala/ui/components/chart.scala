package app.paperhands.components

import org.scalajs.dom.raw.HTMLElement

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.echarts._

import scala.scalajs.js

object Chart {
  case class Props(
      opts: js.Object
  )

  object Props {
    def fromTimeSeries() = {}
  }

  case class State()

  class Backend($ : BackendScope[Props, State]) {
    def domElement =
      $.getDOMNode.map(dom => dom.toElement.get.asInstanceOf[HTMLElement])

    def initChart(p: Props): Callback =
      domElement.map { el =>
        val chart = echarts.init(el)
        chart.setOption(p.opts)
        chart
      } >> Callback.log("initialized chart")

    def mounted: Callback =
      ($.props >>= initChart)

    def update(np: Props): Callback =
      Callback.log("Got new props") >>
        domElement.map { el =>
          val chart = echarts.getInstanceByDom(el)
          chart.setOption(np.opts)
        } >> Callback.log("updated chart")

    def render(props: Props, state: State): VdomElement = {
      <.div(
        ^.className := "chart-target"
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .componentWillReceiveProps(scope => scope.backend.update(scope.nextProps))
    .build

  def apply(props: Props) = Component(props)
}
