package app.paperhands.chart

import org.scalajs.dom
import org.scalajs.dom.document

import app.paperhands.echarts._

import cats.effect._
import cats.syntax._

class Chart(echart: EChart) {
  def setOption(o: ChartOptions): IO[Unit] =
    IO(echart.setOption(o))

  def clear: IO[Unit] =
    IO(echart.clear)

  def dispose: IO[Unit] =
    IO(echart.dispose)
}

object Chart {
  def resource(sel: String): Resource[IO, Chart] =
    Resource.make {
      init(sel)
    } { chart =>
      IO(println(s"removing chart from #$sel")) *>
        chart.clear *>
        chart.dispose
    }

  private def init(sel: String): IO[Chart] =
    for {
      el <- IO(document.getElementById("root"))
      echart <- IO(echarts.init(el))
    } yield new Chart(echart)
}
