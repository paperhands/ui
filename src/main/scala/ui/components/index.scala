package app.paperhands.components

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.extra.Ajax
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.config.Config
import app.paperhands.model._
import app.paperhands.net._

import scala.scalajs.js
import scala.scalajs.js.JSON

object IndexPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State(loading: Boolean, trending: Seq[Trending])

  def renderTrending(t: Trending) =
    <.tr(
      <.td(t.symbol),
      <.td(t.desc),
      <.td(t.pos.toString),
      <.td(t.oldPos.toString),
      <.td(t.changePerc.toString)
    )

  val TrendingTable = ScalaComponent
    .builder[Seq[Trending]]
    .render_P(trending =>
      <.table(
        <.tbody(
          trending.map(renderTrending(_)): _*
        )
      )
    )
    .build

  class Backend($ : BackendScope[Props, State]) {
    def setTrending(body: String): Callback = {
      val trending = JSON.parse(body).asInstanceOf[js.Array[Trending]]
      $.modState(_.copy(trending = trending.toSeq))
    }

    def loadTrending: Callback =
      Net.getTrending.onComplete { xhr =>
        stopLoading >> Callback.log(xhr) >> setTrending(xhr.responseText)
      }.asCallback

    def setLoading(v: Boolean) =
      $.modState(_.copy(loading = v))

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def mounted: Callback =
      Callback.log("Mounted index") >>
        startLoading >>
        loadTrending

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl
      val proxy = props.proxy()
      val trending = state.trending

      <.div(
        <.span("LOADING").when(state.loading),
        ctl.link(AppRouter.Details("gme", "1day"))("got to details"),
        TrendingTable(trending)
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, List()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
