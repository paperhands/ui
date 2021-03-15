package app.paperhands.main

import org.scalajs.dom
import org.scalajs.dom.document

import app.paperhands.chart._
import app.paperhands.echarts._
import app.paperhands.net.Net

import cats.effect._

object TutorialApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    loop.as(ExitCode.Success)

  def loop() =
    for {
      _ <- IO(println("hi"))
      chart <- Chart.init("root")
      _ <- chart.setOption(opts)
      _ <- Net.querySomething
    } yield ()

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
