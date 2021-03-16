package app.paperhands.main

package scalajsApp

import japgolly.scalajs.react.{React, ReactDOM}
import org.scalajs.dom

import scalajs.js
import scalajs.js.annotation._
import japgolly.scalajs.react.vdom.html_<^._
import app.paperhands.router.AppRouter

object ReactApp {

  //@JSExport
  def main(args: Array[String]): Unit = {
    val target = dom.document.getElementById("target")
    AppRouter.router().renderIntoDOM(target)
  }
}
