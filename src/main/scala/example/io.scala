package app.paperhands.io

import cats.effect._

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext.Implicits

trait IOContext {
  implicit def executionContext: ExecutionContext = Implicits.queue
  implicit val cs = IO.contextShift(executionContext)
}
