package app.paperhands.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.CatsReact._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

case class State(items: List[String], text: String)

object Cmp {
  val TodoList = ScalaComponent
    .builder[List[String]]
    .render_P(items => <.ul(items.map(<.li(_)): _*))
    .build

  val ST =
    ReactS.Fix[State] // Let's use a helper so that we don't have to specify the
  //   state type everywhere.

  def acceptChange(e: ReactEventFromInput) =
    ST.mod(
      _.copy(text = e.target.value)
    ) // A pure state modification. State value is provided when run.

  def handleSubmit(e: ReactEventFromInput) = (
    ST.retM(
      e.preventDefaultCB
    ) // Lift a Callback effect into a shape that allows composition
    //   with state modification.
      >> // Use >> to compose. It's flatMap (>>=) that ignores input.
        ST.mod(s => State(s.items :+ s.text, ""))
          .liftCB // Here we lift a pure state modification into a shape that
  )

  val TodoApp = ScalaComponent
    .builder[Unit]
    .initialState(State(Nil, ""))
    .renderS(($, s) =>
      <.div(
        <.h3("TODO"),
        TodoList(s.items),
        <.form(
          ^.onSubmit ==> $.runStateFn(
            handleSubmit
          ), // runState runs a state monad and applies the result.
          <.input( // runStateFn is similar but takes a function-to-a-state-monad.
            ^.onChange ==> $.runStateFn(
              acceptChange
            ), // In these cases, the function will be fed the JS event.
            ^.value := s.text
          ),
          <.button("Add #", s.items.length + 1)
        )
      )
    )
    .build
}
