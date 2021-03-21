package app.paperhands.model

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

trait Content extends js.Object {
  val id: String
  val kind: String
  val source: String
  @JSName("parent_id") val parentID: String
  val permalink: String
  val author: String
  val body: String
  @JSName("created_time") val createdTime: String
  val parsed: ContentMeta
}

trait ContentMeta extends js.Object {
  val symbols: js.Array[String]
  val sentiment: Int
}
