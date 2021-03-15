package app.paperhands.net

import cats._
import cats.effect._
import cats.implicits._

import sttp.client3._
import sttp.client3.impl.cats._

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext.Implicits

object Net {
  implicit def executionContext: ExecutionContext = Implicits.queue
  implicit val cs = IO.contextShift(executionContext)
  // implicit val timer = IO.timer(executionContext)
  val backend = FetchCatsBackend[IO]()

  def querySomething: IO[Unit] = {
    val request =
      basicRequest.get(uri"http://localhost:8888/api/v1/quote/details/gme/1day")
    for {
      response <- backend.send(request)
      _ <- IO(println(response.body))
    } yield ()
  }
}
