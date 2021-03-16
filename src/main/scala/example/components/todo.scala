package app.paperhands.components

import diode.react.ModelProxy

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import app.paperhands.router.AppRouter
import app.paperhands.diode._

object Todo {
  case class Props(
      proxy: ModelProxy[AppState],
      ctl: RouterCtl[AppRouter.Page]
  )

  case class State(text: String)

  val TodoList = ScalaComponent
    .builder[List[String]]
    .render_P(items => <.ul(items.map(<.li(_)): _*))
    .build

  class Backend($ : BackendScope[Props, State]) {
    def mounted: Callback =
      Callback.log("Mounted todo")

    def acceptChange(e: ReactEventFromInput) =
      $.modState(_.copy(text = e.target.value))

    def handleSubmit(addItemCB: String => Callback, text: String)(
        e: ReactEventFromInput
    ) =
      e.preventDefaultCB >>
        addItemCB(text) >>
        $.modState(_ => State(""))

    def render(props: Props, state: State): VdomElement = {
      val proxy = props.proxy()
      val items = proxy.items
      val addItemCB: String => Callback = text =>
        props.proxy.dispatchCB(AddItem(text))

      <.div(
        <.h3("TODO"),
        TodoList(items),
        <.form(
          ^.onSubmit ==> handleSubmit(addItemCB, state.text),
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
    .initialState(State(""))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
