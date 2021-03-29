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

  case class State(trending: Seq[Trending])

  def formatPositionMove(t: Trending) = {
    val limit = 20
    val m = if (t.oldPos < 0) limit - t.pos else t.oldPos - t.pos

    val icon = m match {
      case 0          => None
      case _ if m < 0 => Some("fa-arrow-down")
      case _ if m > 0 => Some("fa-arrow-up")
    }
    val color = m match {
      case 0          => ""
      case _ if m < 0 => "has-text-danger-dark"
      case _ if m > 0 => "has-text-success-dark"
    }

    val txt = if (m == 0) "" else s"$m "

    <.td(
      ^.className := color,
      txt,
      icon.map { ic =>
        <.span(
          ^.key := s"__$m-$ic-$color",
          <.i(^.className := s"fas $ic")
        )
      }
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
      val tableK = if (trending.isEmpty) "is-hidden" else ""
      <.div(
        ^.className := "content",
        Loading.Content(trending.isEmpty),
        <.table(
          ^.className := s"table is-hoverable $tableK",
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
      $.props >>= { props =>
        AppDispatch.setLoading(props.proxy, v)
      }

    def startLoading =
      setLoading(true)

    def stopLoading =
      setLoading(false)

    def mounted: Callback =
      startLoading >>
        loadTrending

    def willReceiveProps(np: Props): Callback = {
      if (np.proxy().shouldRefresh)
        startLoading >> loadTrending
      else
        Callback.empty
    }

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
        heroSection(proxy.currentPeriod),
        TrendingTable(ctl, trending)
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(List()))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .componentWillReceiveProps(scope =>
      scope.backend.willReceiveProps(scope.nextProps)
    )
    .build

  def apply(props: Props) = Component(props)
}
