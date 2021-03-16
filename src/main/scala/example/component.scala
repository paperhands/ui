package app.paperhands.component

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

  case class State(items: List[String], text: String)

  val TodoList = ScalaComponent
    .builder[List[String]]
    .render_P(items => <.ul(items.map(<.li(_)): _*))
    .build

  class Backend($ : BackendScope[Props, State]) {
    def mounted: Callback =
      Callback.log("Mounted todo")

    def acceptChange(e: ReactEventFromInput) =
      $.modState(_.copy(text = e.target.value))

    def handleSubmit(e: ReactEventFromInput) =
      e.preventDefaultCB >>
        $.modState(s => State(s.items :+ s.text, ""))

    def render(props: Props, state: State): VdomElement = {
      <.div(
        <.h3("TODO"),
        TodoList(state.items),
        <.form(
          ^.onSubmit ==> handleSubmit,
          <.input(
            ^.onChange ==> acceptChange,
            ^.value := state.text
          ),
          <.button("Add #", state.items.length + 1)
        )
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State(Nil, ""))
    .renderBackend[Backend]
    .componentDidMount(_.backend.mounted)
    .build

  def apply(props: Props) = Component(props)
}
