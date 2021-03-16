package app.paperhands.router

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.component._
import app.paperhands.diode.AppCircuit

object IndexPanel {
  case class Props(
      ctl: RouterCtl[AppRouter.Page]
  )

  val Component = ScalaComponent.static("")(
    <.div(
      Cmp.TodoApp()
    )
  )

  def apply() = Component()
}

object AppRouter {
  sealed trait Page
  case object Index extends Page

  val connection = AppCircuit.connect(m => m.state)

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, Index) ~> renderR(renderIndexPage))
      .notFound(redirectToPage(Index)(Redirect.Replace))
  }

  def renderIndexPage(ctl: RouterCtl[Page]) = {
    connection(proxy => IndexPanel(IndexPanel.Props(proxy, ctl)))
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
