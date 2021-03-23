package app.paperhands.diode

import diode.Action

case class AppState(
    currentPeriod: String,
    autoRefresh: Option[String]
)

case class AppModel(
    state: AppState
)

case class SetInterval(interval: String) extends Action
case class SetAutoRefresh(refresh: Option[String]) extends Action
