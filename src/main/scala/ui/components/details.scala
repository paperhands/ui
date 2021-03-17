package app.paperhands.components

import org.scalajs.dom.raw.HTMLElement

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.echarts._

object DetailsPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      symbol: String,
      interval: String
  )

  case class State()

  val defaultOpts =
    ChartOptions(
      AxisOptions(
        "category",
        List("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
      ),
      AxisOptions("value", List()),
      List(
        Series("line", List(150, 230, 224, 218, 135, 147, 260))
      )
    )

  class Backend($ : BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        <.h1(
          s"Details for ${props.symbol} for ${props.interval}"
        ),
        Chart(Chart.Props(props.proxy, props.ctl, defaultOpts)),
        ctl.link(AppRouter.Index)("got to index")
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(props: Props) = Component(props)
}
