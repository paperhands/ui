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
import scala.scalajs.js.JSON

object DetailsPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page],
      symbol: String,
      interval: String
  )

  case class State(loading: Boolean, details: Option[Details])

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
    def setDetails(body: String): Callback = {
      val details = JSON.parse(body).asInstanceOf[Details]
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

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl

      <.div(
        <.span("LOADING").when(state.loading),
        <.h1(
          s"Details for ${props.symbol} for ${props.interval}"
        ),
        Chart(Chart.Props(defaultOpts)),
        ctl.link(AppRouter.Index)("got to index")
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
