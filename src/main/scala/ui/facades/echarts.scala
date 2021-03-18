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
  def setOption(o: ChartOptions): Unit = js.native
  def showLoading: Unit = js.native
  def hideLoading: Unit = js.native
  def clear: Unit = js.native
  def dispose: Unit = js.native
}

@js.native
trait ChartOptions extends js.Object {
  val xAxis: AxisOptions
  val yAxis: AxisOptions
  val series: js.Array[Series]
}

object ChartOptions {
  import js.JSConverters._

  def apply(
      x: AxisOptions,
      y: AxisOptions,
      series: List[js.Object]
  ): ChartOptions =
    js.Dynamic
      .literal(
        xAxis = x,
        yAxis = y,
        series = series.toJSArray
      )
      .asInstanceOf[ChartOptions]
}

@js.native
trait AxisOptions extends js.Object {
  val `type`: String
  val data: js.Array[String]
}

object AxisOptions {
  import js.JSConverters._

  def apply(t: String, data: js.Array[String]): AxisOptions =
    js.Dynamic
      .literal(
        `type` = t,
        data = data
      )
      .asInstanceOf[AxisOptions]
}

@js.native
trait Series extends js.Object {
  val name: String
  val stack: String
  val smooth: Boolean
  val showSymbol: Boolean
  val lineStyle: LineStyle
  val label: Label
  val `type`: String
  val data: js.Array[Int]
}

@js.native
trait LineStyle extends js.Object {
  var width: Int
}

@js.native
trait Label extends js.Object {
  var show: Boolean
  var position: String
}

object Series {
  import js.JSConverters._

  def apply(t: String, smooth: Boolean, data: js.Array[Int]): Series =
    js.Dynamic
      .literal(
        "type" -> t,
        "smooth" -> smooth,
        "data" -> data
      )
      .asInstanceOf[Series]
}
