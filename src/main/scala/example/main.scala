package app.paperhands.main

import org.scalajs.dom.document

import app.paperhands.router.AppRouter

object App {
  def main(args: Array[String]): Unit = {
    val target = document.getElementById("target")
    AppRouter.router().renderIntoDOM(target)
  }
}
