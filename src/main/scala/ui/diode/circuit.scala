package app.paperhands.diode

import diode._
import diode.react.ReactConnector

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      currentInternal = "1D"
    )
  )

  override protected val actionHandler = composeHandlers(
    new ExpenditurePageHandler(zoomTo(_.state))
  )
}

class ExpenditurePageHandler[M](modelRW: ModelRW[M, AppState])
    extends ActionHandler(modelRW) {
  override def handle = { case SetInterval(interval) =>
    updated(value.copy(currentInternal = interval))
  }
}
