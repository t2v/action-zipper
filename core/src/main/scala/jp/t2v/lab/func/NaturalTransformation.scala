package jp.t2v.lab.func

trait ~>[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}

object ~> {
  def id[F[_]]: F ~> F = new (F ~> F) {
    def apply[A](fa: F[A]) = fa
  }
}