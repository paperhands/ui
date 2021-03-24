package app.paperhands.diode

import diode._
import diode.react.ReactConnector
import scala.scalajs.js.timers.{setInterval, clearInterval}
import scala.concurrent.duration._

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      currentPeriod = "1D",
      autoRefresh = None,
      refreshHandle = None,
      shouldRefresh = false,
      loading = false
    )
  )

  override protected val actionHandler = composeHandlers(
    new ExpenditurePageHandler(zoomTo(_.state))
  )
}

object Ticker {
  def newInterval(s: String) =
    setInterval(AppConnstants.intervalToDuration(s))(
      AppCircuit.dispatch(RefreshTick())
    )
}

class ExpenditurePageHandler[M](modelRW: ModelRW[M, AppState])
    extends ActionHandler(modelRW) {
  override def handle = {
    case SetInterval(interval) =>
      updated(value.copy(currentPeriod = interval, shouldRefresh = true))
    case SetAutoRefresh(refresh) =>
      value.refreshHandle.foreach(clearInterval)

      updated(
        value.copy(
          autoRefresh = refresh,
          refreshHandle = refresh.map(Ticker.newInterval),
          shouldRefresh = false
        )
      )
    case RefreshTick() =>
      updated(
        value.copy(shouldRefresh = true)
      )
    case SetLoading(v) =>
      updated(
        value.copy(loading = v, shouldRefresh = false)
      )
  }
}
