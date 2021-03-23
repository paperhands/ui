package app.paperhands.diode

import diode._
import diode.react.ReactConnector

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      currentPeriod = "1D",
      autoRefresh = None
    )
  )

  override protected val actionHandler = composeHandlers(
    new ExpenditurePageHandler(zoomTo(_.state))
  )
}

class ExpenditurePageHandler[M](modelRW: ModelRW[M, AppState])
    extends ActionHandler(modelRW) {
  override def handle = {
    case SetInterval(interval) =>
      updated(value.copy(currentPeriod = interval))
    case SetAutoRefresh(refresh) =>
      updated(value.copy(autoRefresh = refresh))
  }
}
