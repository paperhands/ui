package app.paperhands.model

import scala.scalajs.js
import scala.scalajs.js.`|`
import scala.scalajs.js.annotation.JSName

import monocle.Lens
import monocle.macros.syntax.all._

trait Popularity extends js.Object {
  val symbol: String
  val mentions: Int
  val engagements: Int
}

trait Timeseries extends js.Object {
  val data: js.Array[Int]
  val titles: js.Array[String]
}

object TimeseriesLens {
  val data = Lens[Timeseries, js.Array[Int]](_.data)(a =>
    t =>
      js.Dynamic
        .literal(
          data = a,
          titles = t.titles
        )
        .asInstanceOf[Timeseries]
  )

  val titles = Lens[Timeseries, js.Array[String]](_.titles)(a =>
    t =>
      js.Dynamic
        .literal(
          data = t.data,
          titles = a
        )
        .asInstanceOf[Timeseries]
  )
}

trait Details extends js.Object {
  val symbol: String
  val desc: js.UndefOr[String]
  @JSName("current_price") val currentPrice: Double
  val mentions: Timeseries
  val engagements: Timeseries
  val sentiments: Timeseries
  val price: Timeseries
  val popularity: Popularity
}

object DetailsLens {
  val mentions = Lens[Details, Timeseries](_.mentions)(t =>
    d =>
      js.Dynamic
        .literal(
          symbol = d.symbol,
          desc = d.desc,
          current_price = d.currentPrice,
          mentions = t,
          engagements = d.engagements,
          sentiments = d.sentiments,
          price = d.price,
          popularity = d.popularity
        )
        .asInstanceOf[Details]
  )

  val engagements = Lens[Details, Timeseries](_.engagements)(t =>
    d =>
      js.Dynamic
        .literal(
          symbol = d.symbol,
          desc = d.desc,
          current_price = d.currentPrice,
          mentions = d.mentions,
          engagements = t,
          sentiments = d.sentiments,
          price = d.price,
          popularity = d.popularity
        )
        .asInstanceOf[Details]
  )

  val sentiments = Lens[Details, Timeseries](_.sentiments)(t =>
    d =>
      js.Dynamic
        .literal(
          symbol = d.symbol,
          desc = d.desc,
          current_price = d.currentPrice,
          mentions = d.mentions,
          engagements = d.engagements,
          sentiments = t,
          price = d.price,
          popularity = d.popularity
        )
        .asInstanceOf[Details]
  )

  val price = Lens[Details, Timeseries](_.price)(t =>
    d =>
      js.Dynamic
        .literal(
          symbol = d.symbol,
          desc = d.desc,
          current_price = d.currentPrice,
          mentions = d.mentions,
          engagements = d.engagements,
          sentiments = d.sentiments,
          price = t,
          popularity = d.popularity
        )
        .asInstanceOf[Details]
  )
}
