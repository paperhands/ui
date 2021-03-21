package app.paperhands.model

import scala.scalajs.js.{Date, JSON}

import app.paperhands.dateformat.dateFormat

object Model {
  def as[A](body: String): A =
    JSON.parse(body).asInstanceOf[A]
}

object Parse {
  def dateFromString(s: String) =
    new Date(Date.parse(s))
}

object Format {
  def fmtDateFor(d: Date, period: String): String = {
    // dddd, mmmm dS, yyyy, hh:MM:ss TT
    val f = period match {
      case "1D" => "HH:MM"
      case "5D" => "dddd"
      case "1W" => "dddd"
      case "1M" => "mmm. d"
      case "6M" => "mmm. d, yy"
      case "1Y" => "mmm. d, yy"
      case _    => "dddd, mmmm dS, yyyy, HH:MM:ss"
    }

    dateFormat(d, f)
  }
}
