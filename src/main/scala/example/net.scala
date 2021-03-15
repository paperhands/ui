package app.paperhands.net

import cats._
import cats.effect._
import cats.implicits._

import sttp.client3._
import sttp.client3.impl.cats._

import app.paperhands.io.IOContext

object Net extends IOContext {
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
