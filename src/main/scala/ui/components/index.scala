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

object IndexPage {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State(loading: Boolean, text: String)

  val IndexPageList = ScalaComponent
    .builder[List[String]]
    .render_P(items => <.ul(items.map(<.li(_)): _*))
    .build

  class Backend($ : BackendScope[Props, State]) {
    def loadTrending: Callback =
      Ajax(
        "GET",
        Config.api.prefixPath("/api/v1/content/sample/gme")
      ).setRequestContentTypeJsonUtf8
        .send("")
        .onComplete { xhr =>
          stopLoading >>
            Callback.log(xhr)
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
        loadTrending

    def acceptChange(e: ReactEventFromInput) =
      $.modState(_.copy(text = e.target.value))

    def handleSubmit(addItemCB: String => Callback)(
        e: ReactEventFromInput
    ) =
      e.preventDefaultCB >>
        ($.state.map(_.text) >>= addItemCB) >>
        $.modState(_ => State(false, ""))

    def addItemCB(props: Props)(text: String) =
      props.proxy.dispatchCB(AddItem(text))

    def render(props: Props, state: State): VdomElement = {
      val ctl = props.ctl
      val proxy = props.proxy()
      val items = proxy.items

      <.div(
        ctl.link(AppRouter.Details("gme", "1day"))("got to details"),
        <.h3("TODO"),
        IndexPageList(items),
        <.form(
          ^.onSubmit ==> handleSubmit(addItemCB(props)),
          <.input(
            ^.onChange ==> acceptChange,
            ^.value := state.text
          ),
          <.button("Add #", items.length + 1)
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(false, ""))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
