package app.paperhands.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import scala.scalajs.js

object Fa {
  case class Props(
      icon: String,
      style: String
  )

  case class State()

  class Backend($ : BackendScope[Props, State]) {
    def render(props: Props, state: State): VdomElement = {
      val st = props.style
      val ic = props.icon

      <.span(
        ^.key := s"__-$st-$ic",
        <.i(^.className := s"$st $ic")
      )
    }
  }

  val Component = ScalaComponent
    .builder[Props]
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(
      icon: String,
      style: String = "fas"
  ) = Component(Props(icon, style))
}
