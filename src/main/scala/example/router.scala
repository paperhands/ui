package app.paperhands.router

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.component._

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

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, Index) ~> renderR(renderIndexPage))
      .notFound(redirectToPage(Index)(Redirect.Replace))
  }

  def renderIndexPage(ctl: RouterCtl[Page]) = {
    IndexPanel()
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
