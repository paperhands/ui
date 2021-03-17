package app.paperhands.router

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.components._
import app.paperhands.diode.AppCircuit

object AppRouter {
  sealed trait Page
  case object Index extends Page
  case class Details(symbol: String, period: String) extends Page

  val connection = AppCircuit.connect(m => m.state)

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, Index) ~> renderR(renderIndexPage)
      | dynamicRouteCT(
        ("details" / string("[a-z]+") / string("[0-9]+[a-z]+"))
          .caseClass[Details]
      ) ~> dynRenderR(renderDetailsPage))
      .notFound(redirectToPage(Index)(Redirect.Replace))
  }

  def renderIndexPage(ctl: RouterCtl[Page]) = {
    connection(proxy => Todo(Todo.Props(proxy, ctl)))
  }

  def renderDetailsPage(
      params: Details,
      ctl: RouterCtl[Page]
  ) = {
    connection(proxy =>
      DetailsPage(DetailsPage.Props(proxy, ctl, params.symbol, params.period))
    )
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
