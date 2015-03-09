package jp.t2v.lab.func

trait HFunctor[F[_[_]]] {
  def map[A[_], B[_]](fa: F[A])(f: A ~> B): F[B]
}