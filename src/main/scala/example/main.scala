package app.paperhands.main

import org.scalajs.dom
import org.scalajs.dom.document

import app.paperhands.echarts._

object TutorialApp {
  def main(args: Array[String]): Unit =
    dom.window.addEventListener("load", onload)

  def onload(e: dom.Event) = {
    val d = document.getElementById("root")
    val chart = echarts.init(d)
    chart.setOption(opts)
  }

  def opts =
    ChartOptions(
      AxisOptions(
        "category",
        List("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
      ),
      AxisOptions("value", List()),
      List(
        Series("line", List(150, 230, 224, 218, 135, 147, 260))
      )
    )
}
