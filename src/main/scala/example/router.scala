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
  case object Details extends Page

  val connection = AppCircuit.connect(m => m.state)

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, Index) ~> renderR(renderIndexPage)
      | staticRoute("#chart", Details) ~> renderR(renderDetailsPage))
      .notFound(redirectToPage(Index)(Redirect.Replace))
  }

  def renderIndexPage(ctl: RouterCtl[Page]) = {
    connection(proxy => Todo(Todo.Props(proxy, ctl)))
  }

  def renderDetailsPage(ctl: RouterCtl[Page]) = {
    connection(proxy => DetailsPage(DetailsPage.Props(proxy, ctl)))
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
