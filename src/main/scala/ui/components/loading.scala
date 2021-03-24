package app.paperhands.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import diode.react.ModelProxy
import app.paperhands.diode._

object Loading {
  val Modal = ScalaComponent
    .builder[ModelProxy[AppState]]
    .render_P { proxy =>
      val isActive = proxy().loading
      val klass = if (isActive) "" else "is-hidden"

      <.div(
        ^.className := s"loading-bar $klass",
        <.progress(
          ^.className := "progress is-small is-primary",
          ^.max := "100",
          "1%"
        )
      )
    }
    .build
}
