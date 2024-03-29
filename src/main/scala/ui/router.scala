package app.paperhands.router

import app.paperhands.components._
import app.paperhands.diode.AppCircuit
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

object AppRouter {
  sealed trait Page
  case object Index extends Page
  case object Labeling extends Page
  case class Details(symbol: String) extends Page
  case class Samples(symbol: String) extends Page

  val connection = AppCircuit.connect(m => m.state)

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, Index) ~> renderR(renderIndexPage)
      | dynamicRouteCT(
        ("#details" / string("[a-z]+")).caseClass[Details]
      ) ~> dynRenderR(renderDetailsPage)
      | dynamicRouteCT(
        ("#samples" / string("[a-z]+")).caseClass[Samples]
      ) ~> dynRenderR(renderSamplesPage)
      | staticRoute("#labeling", Labeling) ~> renderR(renderLabelingPage))
      .notFound(redirectToPage(Index)(Redirect.Replace))
  }

  def renderIndexPage(ctl: RouterCtl[Page]) = {
    connection(proxy =>
      Layout(proxy, ctl, IndexPage(IndexPage.Props(proxy, ctl)))
    )
  }

  def renderDetailsPage(
      params: Details,
      ctl: RouterCtl[Page]
  ) = {
    connection(proxy =>
      Layout(
        proxy,
        ctl,
        DetailsPage(DetailsPage.Props(proxy, ctl, params.symbol))
      )
    )
  }

  def renderSamplesPage(
      params: Samples,
      ctl: RouterCtl[Page]
  ) = {
    connection(proxy =>
      Layout(
        proxy,
        ctl,
        SamplesPage(SamplesPage.Props(proxy, ctl, false, Some(params.symbol)))
      )
    )
  }

  def renderLabelingPage(
      ctl: RouterCtl[Page]
  ) = {
    connection(proxy =>
      Layout(
        proxy,
        ctl,
        SamplesPage(SamplesPage.Props(proxy, ctl, true, None))
      )
    )
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
