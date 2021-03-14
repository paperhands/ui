package tutorial.webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom
import org.scalajs.dom.document

@JSImport("echarts", JSImport.Namespace)
@js.native
object echarts extends js.Object {
  def init(e: dom.Element): EChart = js.native
}

@js.native
@JSImport("echarts", "echartsInstance")
class EChart extends js.Object {
  def setOption(o: EChartOptions): Unit = js.native
}

@js.native
trait EChartOptions extends js.Object {
  val url: String
}

object EChartOptions {
  def apply(): EChartOptions =
    js.Dynamic
      .literal(
      )
      .asInstanceOf[EChartOptions]
}

object TutorialApp {
  def main(args: Array[String]): Unit =
    dom.window.addEventListener("load", onload)

  def onload(e: dom.Event) = {
    val d = document.getElementById("root")
    val chart = echarts.init(d)
    chart.setOption(EChartOptions())
    println("Hello world!")
  }
}
