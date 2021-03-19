package app.paperhands.components

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.extra.Ajax
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._
import app.paperhands.model._
import app.paperhands.net._

import scala.scalajs.js

object IndexPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State(loading: Boolean, trending: Seq[Trending])

  def renderTrending(ctl: RouterCtl[AppRouter.Page], t: Trending) =
    <.tr(
      <.td(
        ctl.link(AppRouter.Details(t.symbol.toLowerCase, "1day"))(t.symbol)
      ),
      <.td(t.desc),
      <.td(t.pos.toString),
      <.td(t.oldPos.toString),
      <.td(t.changePerc.toString)
    )

  val TrendingTable = ScalaComponent
    .builder[(RouterCtl[AppRouter.Page], Seq[Trending])]
    .render_P { case (ctl, trending) =>
      <.table(
        ^.className := "table",
        <.tbody(
          trending.map(renderTrending(ctl, _)): _*
        )
      )
    }
    .build

  class Backend($ : BackendScope[Props, State]) {
    def setTrending(body: String): Callback = {
      val trending = Model.as[js.Array[Trending]](body)
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
        Loading.Modal().when(state.loading),
        TrendingTable(ctl, trending)
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
