package app.paperhands.chart

import app.paperhands.model._

import scala.scalajs.js
import scala.scalajs.js.`|`

import typings.zrender.zrender.LinearGradient // not used for now, area style only expects string as color atm
import typings.echarts.echarts._
import typings.echarts.echarts.EChartOption._
import typings.echarts.echarts.EChartOption.BasicComponents.CartesianAxis.DataObject
import typings.echarts.echarts.EChartOption.BasicComponents.CartesianAxis.Type
import typings.echarts.anon
import typings.echarts.anon.ShadowBlur
import typings.echarts.anon.BorderRadius
import typings.echarts.anon.ColorOpacity
import typings.echarts.mod.graphic
import typings.echarts.echartsStrings
import typings.echarts.echarts.EChartOption.BasicComponents.CartesianAxis.PointerLabel

object ChartOpts {
  def optsFromTimeseries(
      legendLabel: String
  )(ts: Timeseries): EChartOption[Series] = {
    val as = ColorOpacity().setOpacity(0.8).setColor("rgba(128, 255, 165)")

    val sb = ShadowBlur().setWidth(0)
    val label = BorderRadius().setShow(false).setPosition("top")
    val series = SeriesLine_()
      .setType("line")
      .setData(ts.data.map(_.toDouble))
      .setSmooth(true)
      .setLineStyle(sb)
      .setLabel(label)
      .setAreaStyle(as)
      .setShowSymbol(false)
      .setEmphasis(
        js.Dynamic.literal("focus" -> "series").asInstanceOf[anon.`8`]
      )

    val xAxis = XAxis()
      .setType(Type.category)
      .setData(ts.titles)
    val yAxis = YAxis()
      .setType(Type.value)
      .setData(js.Array())

    val tb = js.Dynamic.literal(
      "feature" -> js.Dynamic.literal(
        "saveAsImage" -> js.Object()
      )
    )

    val grid = Grid()
      .setContainLabel(true)
      .setLeft("3%")
      .setRight("4%")
      .setBottom("3%")

    val legend = Legend_()
      .setData(js.Array(legendLabel))

    val pl = PointerLabel().setBackgroundColor("#6a7985")
    val ap = Tooltip
      .AxisPointer()
      .setType(echartsStrings.line)
      .setLabel(pl)
    val tooltip = Tooltip_()
      .setTrigger(echartsStrings.axis)
      .setAxisPointer(ap)

    val title = EChartTitleOption()
      .setText(legendLabel)

    val opts = EChartOption_[Series]()
      .setXAxis(xAxis)
      .setYAxis(yAxis)
      .setSeries(js.Array(series))
      .setToolbox(tb)
      .setGrid(grid)
      .setLegend(legend)
      .setTooltip(tooltip)
      .setTitle(title)

    opts
  }

}
