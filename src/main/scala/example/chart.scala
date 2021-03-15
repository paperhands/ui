package app.paperhands.chart

import org.scalajs.dom
import org.scalajs.dom.document

import app.paperhands.echarts._

import cats.effect._

class Chart(echart: EChart) {
  def setOption(o: ChartOptions): IO[Unit] =
    IO(echart.setOption(o))
}

object Chart {
  def init(sel: String): IO[Chart] =
    for {
      el <- IO(document.getElementById("root"))
      echart <- IO(echarts.init(el))
    } yield new Chart(echart)
}
