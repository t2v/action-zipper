package jp.t2v.lab.play2

import play.api.mvc._
import jp.t2v.lab.func.{HApply, ~>}
import scala.concurrent.Future

package object actzip {

  implicit val actionBuilderHApply: HApply[ActionBuilder] = new HApply[ActionBuilder] {
    def apply2[A1[_], A2[_], Z[_]](a1: ActionBuilder[A1], a2: ActionBuilder[A2])(f: λ[a => (A1[a], A2[a])] ~> Z) =
      new ActionBuilder[Z] {
        def invokeBlock[C](request: Request[C], block: Z[C] => Future[Result]) =
          a1.invokeBlock[C](request, b1 =>
            a2.invokeBlock[C](request, b2 =>
              block(f((b1, b2)))
            )
          )
      }

    def map[A[_], B[_]](fa: ActionBuilder[A])(f: A ~> B): ActionBuilder[B] = new ActionBuilder[B] {
      def invokeBlock[C](request: Request[C], block: B[C] => Future[Result]) =
        fa.invokeBlock[C](request, a => block(f(a)))
    }
  }

  implicit class HApplyOps[F[_[_]], A1[_]](val self: F[A1])(implicit val F: HApply[F]) {

    final def zip[A2[_]](f: F[A2]): F[λ[a => (A1[a], A2[a])]] = F.tuple2(self, f)

//    final def zip[A2[_], A3[_]](f: F[λ[a => (A2[a], A3[a])]]): F[λ[a => (A1[a], A2[a], A3[a])]] = F.tuple2(self, f)

  }

}
