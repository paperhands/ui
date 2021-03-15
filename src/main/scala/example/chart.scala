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
      create(sel)
    } { chart =>
      IO(println(s"Removing chart from #$sel")) *>
        chart.clear *>
        chart.dispose
    }

  def create(sel: String): IO[Chart] =
    for {
      el <- IO(document.getElementById(sel))
      echart <- IO(echarts.init(el))
    } yield new Chart(echart)
}
