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

  def formatPositionMove(t: Trending) = {
    val m = t.oldPos - t.pos
    val icon = if (m < 0) "fa-arrow-down" else "fa-arrow-up"
    val color = if (m < 0) "has-text-danger-dark" else "has-text-success-dark"

    <.td(
      ^.className := color,
      s"$m ",
      <.i(^.className := s"fas $icon")
    )
  }

  def formatPerc(t: Trending) = {
    val perc = t.changePerc
    val color =
      if (perc < 0) "has-text-danger-dark" else "has-text-success-dark"

    <.td(
      ^.className := color,
      s"$perc%"
    )
  }

  val hideMobile = ^.className := "is-hidden-mobile"

  def renderTrending(ctl: RouterCtl[AppRouter.Page], t: Trending) =
    <.tr(
      ^.className := "is-clickable",
      ^.onClick --> ctl.set(
        AppRouter.Details(t.symbol.toLowerCase)
      ),
      <.td(
        ^.className := "has-text-weight-bold",
        s"$$${t.symbol}"
      ),
      <.td(hideMobile, t.desc),
      formatPositionMove(t),
      formatPerc(t),
      <.td(t.popularity)
    )

  val thead =
    <.thead(
      <.tr(
        <.th(<.abbr(^.title := "Stonk", "$")),
        <.th(hideMobile, <.abbr(^.title := "Description", "Desc")),
        <.th(<.abbr(^.title := "Popularity move", "Move")),
        <.th(<.abbr(^.title := "Popularity change", "Change")),
        <.th(<.abbr(^.title := "Comment volume", "Volume"))
      )
    )

  val TrendingTable = ScalaComponent
    .builder[(RouterCtl[AppRouter.Page], Seq[Trending])]
    .render_P { case (ctl, trending) =>
      <.div(
        ^.className := "content",
        <.table(
          ^.className := "table is-hoverable",
          thead,
          <.tbody(
            trending.map(renderTrending(ctl, _)): _*
          )
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
      $.props >>= { props =>
        val proxy = props.proxy()
        val period = proxy.currentPeriod

        Net
          .getTrending(period)
          .onComplete { xhr =>
            stopLoading >> setTrending(xhr.responseText)
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
        loadTrending

    def willReceiveProps: Callback =
      startLoading >>
        loadTrending

    def heroSection(period: String) =
      <.section(
        ^.className := "hero is-link mb-6",
        <.div(
          ^.className := "hero-body",
          <.p(
            ^.className := "title",
            "Popularity of stonks on WSB"
          ),
          <.div(
            ^.className := "subtitle",
            <.p(
              "Compared to the previous period of ",
              <.span(^.className := "has-text-weight-bold", period)
            )
          )
        )
      )

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl
      val proxy = props.proxy()
      val trending = state.trending

      <.div(
        Loading.Modal(state.loading),
        Tabs(Tabs.Props(props.proxy)),
        heroSection(proxy.currentPeriod),
        TrendingTable(ctl, trending)
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, List()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .componentWillReceiveProps(_.backend.willReceiveProps)
    .build

  def apply(props: Props) = Component(props)
}
