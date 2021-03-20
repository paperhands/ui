package app.paperhands.chart

import app.paperhands.model._

import scala.scalajs.js
import scala.scalajs.js.`|`
import app.paperhands.echarts._

object ChartOpts {
  import js.JSConverters._

  val obj = js.Dynamic.literal

  def gradColor(off: Int, color: String) =
    obj(
      "offset" -> off,
      "color" -> color
    )

  def gradColors(pairs: (Int, String)*) =
    pairs.map { case (o, c) => gradColor(o, c) }.toJSArray

  def gradient(c1: String, c2: String) =
    new echarts.graphic.LinearGradient(
      0,
      0,
      0,
      1,
      js.Array(
        gradColor(0, c1),
        gradColor(1, c2)
      ),
      false
    )

  def series(data: js.Array[Int], name: String, c1: String, c2: String) =
    obj(
      "type" -> "line",
      "name" -> name,
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
        "color" -> gradient(c1, c2)
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

  def engagementMentionsSeries(
      engagements: Timeseries,
      mentions: Timeseries
  ): js.Array[js.Object] =
    js.Array(
      series(
        engagements.data,
        "Engagement",
        "rgba(0, 221, 255)",
        "rgba(77, 119, 255)"
      ),
      series(
        mentions.data,
        "Mentions",
        "rgba(128, 255, 165)",
        "rgba(1, 191, 236)"
      )
    )

  def sentimentSeries(ts: Timeseries): js.Array[js.Object] =
    js.Array(
      series(ts.data, "Sentiment", "rgba(128, 255, 165)", "green")
    )

  def optsFromTimeseriesAndSeries(
      legend: js.Array[String],
      ts: Timeseries,
      series: js.Array[js.Object]
  ): js.Object =
    obj(
      "xAxis" -> xAxis(ts),
      "yAxis" -> yAxis,
      "series" -> series,
      "toolbox" -> toolbox,
      "grid" -> obj(
        "containLabel" -> true,
        "left" -> "3%",
        "right" -> "4%",
        "bottom" -> "3%"
      ),
      "legend" -> obj(
        "data" -> legend
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
      )
    )

}
