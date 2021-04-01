package app.paperhands.main

import app.paperhands.router.AppRouter
import org.scalajs.dom.document

object App {
  def main(args: Array[String]): Unit = {
    val target = document.getElementById("target")
    AppRouter.router().renderIntoDOM(target)
  }
}
