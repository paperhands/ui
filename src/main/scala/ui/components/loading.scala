package app.paperhands.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

object Loading {
  val Modal = ScalaComponent
    .builder[Boolean]
    .render_P { isActive =>
      val klass = if (isActive) "is-active" else ""

      <.div(
        ^.className := s"modal $klass",
        <.div(
          ^.className := "modal-background"
        ),
        <.div(
          ^.className := "modal-content",
          <.div(
            ^.className := "box",
            <.progress(
              ^.className := "progress is-small is-primary",
              ^.max := "100",
              "1%"
            )
          )
        )
      )
    }
    .build
}
