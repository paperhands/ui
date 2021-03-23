package app.paperhands.diode

import diode.Action

import scala.scalajs.js.timers.SetIntervalHandle
import scala.concurrent.duration._

case class AppState(
    currentPeriod: String,
    autoRefresh: Option[String],
    refreshHandle: Option[SetIntervalHandle],
    tickNumber: Int
)

case class AppModel(
    state: AppState
)

case class SetInterval(interval: String) extends Action
case class SetAutoRefresh(refresh: Option[String]) extends Action
case class RefreshTick() extends Action

object AppConnstants {
  val viewRanges = List("1D", "5D", "1W", "1M", "6M", "1Y")
  val refreshIntervals = List("off", "1 min", "5 min", "10 min", "30 min")

  def intervalToDuration(s: String) =
    s match {
      case "1 min"  => 1.minute
      case "5 min"  => 5.minute
      case "10 min" => 10.minutes
      case "30 min" => 30.minutes
    }
}
