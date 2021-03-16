package app.paperhands.diode

import diode._
import diode.react.ReactConnector

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      items = List(),
      isLoading = false
    )
  )

  override protected val actionHandler = composeHandlers(
    new ExpenditurePageHandler(zoomTo(_.state))
  )
}

class ExpenditurePageHandler[M](modelRW: ModelRW[M, AppState])
    extends ActionHandler(modelRW) {
  override def handle = {
    case AddItem(text) =>
      updated(value.copy(items = value.items :+ text))
    case StartLoading() =>
      updated(value.copy(isLoading = true))
    case StopLoading() =>
      updated(value.copy(isLoading = false))
  }
}
