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

  def querySomething = {
    val request =
      basicRequest.get(uri"http://localhost:8888/api/v1/quote/details/gme/1day")
    val backend = FetchBackend()
    // val backend = FetchCatsBackend[IO]()
  }
}
