package app.paperhands.model

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

trait Popularity extends js.Object {
  val symbol: String
  val mentions: Int
  val engagements: Int
}

trait Timeseries extends js.Object {
  val data: js.Array[Int]
  val titles: js.Array[String]
}

trait Details extends js.Object {
  val mentions: Timeseries
  val engagements: Timeseries
  val sentiments: Timeseries
  val price: Timeseries
  val popularity: Popularity
}
