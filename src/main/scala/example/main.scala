package app.paperhands.main

import org.scalajs.dom
import org.scalajs.dom.document

import app.paperhands.chart._
import app.paperhands.echarts._
import app.paperhands.net.Net
import app.paperhands.component._

import cats.effect._

object TutorialApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    loop.as(ExitCode.Success)

  def loop =
    Chart.resource("root").use { chart =>
      for {
        _ <- IO(println("hi"))
        _ <- chart.setOption(opts)
        _ <- render
        // _ <- Net.querySomething
      } yield ()
    }

  def render: IO[Unit] =
    for {
      v <- SVar[Int](0)
      c <- Component("a", Map("keke" -> v.get.map(_.toString)))
      _ <- c.mount("root2")
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
