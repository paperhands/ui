package app.paperhands.model

import scala.scalajs.js.{Date, JSON}

object Model {
  def as[A](body: String): A =
    JSON.parse(body).asInstanceOf[A]
}

object Parse {
  def dateFromString(s: String) =
    new Date(Date.parse(s))
}

object Format {
  // TODO this is also ugly, think about a better way
  def formatDateFor(d: Date, period: String): String =
    period match {
      // TODO add more formatters based on period string
      case _ => s"${d.getHours}:${d.getMinutes}"
    }
}
