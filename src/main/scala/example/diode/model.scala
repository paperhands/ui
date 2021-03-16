package app.paperhands.diode

import diode.Action

case class AppState(
    items: List[String],
    isLoading: Boolean
)

case class AppModel(
    state: AppState
)

case class AddItem(text: String) extends Action
case class StartLoading() extends Action
case class StopLoading() extends Action
