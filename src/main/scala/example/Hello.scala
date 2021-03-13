package tutorial.webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom
import org.scalajs.dom.document

@JSImport("echarts", JSImport.Namespace)
@js.native
object echarts extends js.Object {
  def init(e: dom.Element): Unit = js.native
}

object TutorialApp {
  def main(args: Array[String]): Unit =
    dom.window.addEventListener("load", onload)

  def onload(e: dom.Event) = {
    val d = document.getElementById("root")
    echarts.init(d)
    println("Hello world!")
  }
}
