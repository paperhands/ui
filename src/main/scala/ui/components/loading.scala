package app.paperhands.components

import app.paperhands.diode._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

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

  val Content = ScalaComponent
    .builder[Boolean]
    .render_P { isActive =>
      val klass = if (isActive) "is-active" else "is-hidden"

      <.div(
        ^.className := s"loader-wrapper $klass",
        <.div(
          ^.className := "loader is-loading"
        )
      )
    }
    .build
}
