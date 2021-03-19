package app.paperhands.chart

import app.paperhands.model._

import scala.scalajs.js
import scala.scalajs.js.`|`

object ChartOpts {
  val obj = js.Dynamic.literal

  def series(data: js.Array[Int]) =
    obj(
      "type" -> "line",
      "data" -> data,
      "smooth" -> true,
      "lineStyle" -> obj(
        "width" -> 0
      ),
      "label" -> obj(
        "show" -> true,
        "position" -> "top"
      ),
      "areaStyle" -> obj(
        "opacity" -> 0.8,
        "color" -> "rgba(128, 255, 165)"
      ),
      "showSymbol" -> false,
      "emphasis" -> obj(
        "focus" -> "series"
      )
    )

  val toolbox = obj(
    "feature" -> js.Dynamic.literal(
      "saveAsImage" -> js.Object()
    )
  )
  val yAxis = obj(
    "type" -> "value",
    "data" -> js.Array()
  )

  def xAxis(ts: Timeseries) =
    obj(
      "type" -> "category",
      "data" -> ts.titles
    )

  def optsFromTimeseries(
      legendLabel: String
  )(ts: Timeseries): js.Object = {

    obj(
      "xAxis" -> xAxis(ts),
      "yAxis" -> yAxis,
      "series" -> js.Array(series(ts.data)),
      "toolbox" -> toolbox,
      "grid" -> obj(
        "containLabel" -> true,
        "left" -> "3%",
        "right" -> "4%",
        "bottom" -> "3%"
      ),
      "legend" -> obj(
        "data" -> js.Array(legendLabel)
      ),
      "tooltip" -> obj(
        "trigger" -> "axis",
        "axisPointer" -> obj(
          "type" -> "line",
          "label" -> obj(
            "backgroundColor" -> "#6a7985"
          )
        )
      ),
      "title" -> obj(
        "text" -> legendLabel
      )
    )
  }

}
