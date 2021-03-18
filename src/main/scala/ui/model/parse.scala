package app.paperhands.model

import scala.scalajs.js.JSON

object Model {
  def as[A](body: String): A =
    JSON.parse(body).asInstanceOf[A]
}
