package app.paperhands.echarts

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom

@JSImport("echarts", JSImport.Namespace)
@js.native
object echarts extends js.Object {
  def init(e: dom.Element): EChart = js.native
  val graphic: graphicModule = js.native
}

@js.native
trait graphicModule extends js.Object {
  @js.native
  class LinearGradient(
      x: Int,
      y: Int,
      x2: Int,
      y2: Int,
      colorStops: js.Array[js.Object],
      globalCoord: Boolean
  ) extends js.Object
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
