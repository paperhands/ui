package app.paperhands.component

import cats.effect._
import cats.effect.concurrent.Ref
import app.paperhands.io.IOContext

// create component
// render via IO monad, get a tree
// stateless component can have implicit counter for querying state
// state is stored in a SVar type
// on update SVar updates its version counter
// on get SVar receives impcicit Ref that marks component dirty or not based on version

class SVar[A](state: Ref[IO, A], version: Ref[IO, Int]) {
  def get: IO[A] =
    state.get

  def set(v: A): IO[Unit] =
    version.update(_ + 1) *>
      state.set(v)

  def update(f: A => A): IO[Unit] =
    version.update(_ + 1) *>
      state.update(f(_))
}

object SVar extends IOContext {
  def apply[A](v: A): IO[SVar[A]] =
    for {
      state <- Ref.of[IO, A](v)
      version <- Ref.of[IO, Int](0)
    } yield new SVar[A](state, version)
}

class Component(tag: String, args: Map[String, IO[String]]) {
  def render() =
    IO.unit

  def mount(target: String) =
    IO.unit
}

object Component {
  def apply(tag: String, args: Map[String, IO[String]]): IO[Component] =
    IO(new Component(tag, args))
}
