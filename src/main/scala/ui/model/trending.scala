package app.paperhands.model

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

trait Trending extends js.Object {
  val symbol: String
  val desc: js.UndefOr[String]
  val pos: Int
  @JSName("change_perc") val changePerc: Double
  @JSName("old_pos") val oldPos: Int
  val popularity: Int
}
