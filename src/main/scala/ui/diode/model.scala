package app.paperhands.diode

import diode.Action

import scala.scalajs.js.timers.SetIntervalHandle
import scala.concurrent.duration._
import diode.react.ModelProxy

case class AppState(
    currentPeriod: String,
    autoRefresh: Option[String],
    refreshHandle: Option[SetIntervalHandle],
    shouldRefresh: Boolean,
    loading: Boolean
)

case class AppModel(
    state: AppState
)

case class SetInterval(interval: String) extends Action
case class SetAutoRefresh(refresh: Option[String]) extends Action
case class SetLoading(loading: Boolean) extends Action
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

object AppDispatch {
  def autoRefresh(proxy: ModelProxy[AppState], v: Option[String]) =
    proxy.dispatchCB(SetAutoRefresh(v))

  def setInterval(proxy: ModelProxy[AppState], v: String) =
    proxy.dispatchCB(SetInterval(v))

  def setLoading(proxy: ModelProxy[AppState], v: Boolean) =
    proxy.dispatchCB(SetLoading(v))
}

object AppState {}
