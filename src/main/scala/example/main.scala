package app.paperhands.main

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom
import org.scalajs.dom.document

import app.paperhands.echarts._

object TutorialApp {
  def main(args: Array[String]): Unit =
    dom.window.addEventListener("load", onload)

  def onload(e: dom.Event) = {
    val d = document.getElementById("root")
    val chart = echarts.init(d)
    chart.setOption(EChartOptions())
    println("Hello world!")
  }
}
