package jp.t2v.lab.func

trait HApply[F[_[_]]] extends HFunctor[F] {
  def apply2[A1[_], A2[_], Z[_]](a1: F[A1], a2: F[A2])(f: λ[a => (A1[a], A2[a])] ~> Z): F[Z]

  def apply3[A1[_], A2[_], A3[_], Z[_]](a1: F[A1], a2: F[A2], a3: F[A3])(f: λ[a => (A1[a], A2[a], A3[a])] ~> Z): F[Z] =
    apply2[λ[a => (A1[a], A2[a])], A3, Z](tuple2(a1, a2), a3)(
      new (λ[a => ((A1[a], A2[a]), A3[a])] ~> Z){
        def apply[a](fa: ((A1[a], A2[a]), A3[a])) =
          f((fa._1._1, fa._1._2, fa._2))
      }
    )

  def apply4[A1[_], A2[_], A3[_], A4[_], Z[_]](a1: F[A1], a2: F[A2], a3: F[A3], a4: F[A4])(f: λ[a => (A1[a], A2[a], A3[a], A4[a])] ~> Z): F[Z] =
    apply2[λ[a => (A1[a], A2[a])], λ[a => (A3[a], A4[a])], Z](tuple2(a1, a2), tuple2(a3, a4))(
      new (λ[a => ((A1[a], A2[a]), (A3[a], A4[a]))] ~> Z){
        def apply[a](fa: ((A1[a], A2[a]), (A3[a], A4[a]))) =
          f((fa._1._1, fa._1._2, fa._2._1, fa._2._2))
      }
    )

  def apply5[A1[_], A2[_], A3[_], A4[_], A5[_], Z[_]](a1: F[A1], a2: F[A2], a3: F[A3], a4: F[A4], a5: F[A5])(f: λ[a => (A1[a], A2[a], A3[a], A4[a], A5[a])] ~> Z): F[Z] =
    apply2[λ[a => (A1[a], A2[a])], λ[a => (A3[a], A4[a], A5[a])], Z](tuple2(a1, a2), tuple3(a3, a4, a5))(
      new (λ[a => ((A1[a], A2[a]), (A3[a], A4[a], A5[a]))] ~> Z){
        def apply[a](fa: ((A1[a], A2[a]), (A3[a], A4[a], A5[a]))) =
          f((fa._1._1, fa._1._2, fa._2._1, fa._2._2, fa._2._3))
      }
    )


  def tuple2[A1[_], A2[_]](a1: F[A1], a2: F[A2]): F[λ[a => (A1[a], A2[a])]] =
    apply2[A1, A2, λ[a => (A1[a], A2[a])]](a1, a2)(~>.id[λ[a => (A1[a], A2[a])]])

  def tuple3[A1[_], A2[_], A3[_]](a1: F[A1], a2: F[A2], a3: F[A3]): F[λ[a => (A1[a], A2[a], A3[a])]] =
    apply3[A1, A2, A3, λ[a => (A1[a], A2[a], A3[a])]](a1, a2, a3)(~>.id[λ[a => (A1[a], A2[a], A3[a])]])

  def tuple4[A1[_], A2[_], A3[_], A4[_]](a1: F[A1], a2: F[A2], a3: F[A3], a4: F[A4]): F[λ[a => (A1[a], A2[a], A3[a], A4[a])]] =
    apply4[A1, A2, A3, A4, λ[a => (A1[a], A2[a], A3[a], A4[a])]](a1, a2, a3, a4)(~>.id[λ[a => (A1[a], A2[a], A3[a], A4[a])]])

  def tuple5[A1[_], A2[_], A3[_], A4[_], A5[_]](a1: F[A1], a2: F[A2], a3: F[A3], a4: F[A4], a5: F[A5]): F[λ[a => (A1[a], A2[a], A3[a], A4[a], A5[a])]] =
    apply5[A1, A2, A3, A4, A5, λ[a => (A1[a], A2[a], A3[a], A4[a], A5[a])]](a1, a2, a3, a4, a5)(~>.id[λ[a => (A1[a], A2[a], A3[a], A4[a], A5[a])]])
}
