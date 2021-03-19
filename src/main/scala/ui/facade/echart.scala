package app.paperhands.echarts

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom

@JSImport("echarts", JSImport.Namespace)
@js.native
object echarts extends js.Object {
  def init(e: dom.Element): EChart = js.native
}

@js.native
@JSImport("echarts", "echartsInstance")
class EChart extends js.Object {
  def setOption(o: js.Object): Unit = js.native
  def showLoading: Unit = js.native
  def hideLoading: Unit = js.native
  def clear: Unit = js.native
  def dispose: Unit = js.native
}
