package app.paperhands.diode

import diode.Action

case class AppState(
    currentInternal: String
)

case class AppModel(
    state: AppState
)

case class SetInterval(interval: String) extends Action
