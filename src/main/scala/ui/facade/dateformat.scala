package app.paperhands.dateformat

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom

@JSImport("dateformat", JSImport.Namespace)
@js.native
object dateFormat extends js.Object {
  def apply(d: Date, f: String): String = js.native
}
