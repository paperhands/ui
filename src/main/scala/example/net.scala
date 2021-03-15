package app.paperhands.net

import cats._
import cats.effect._
import cats.implicits._

import sttp.client3._

object Net {
  def querySomething = {
    val request =
      basicRequest.get(uri"http://localhost:8888/api/v1/quote/details/gme/1day")
    val backend = FetchCatsBackend[IO]()
    // val backend = FetchBackend()
  }
}
