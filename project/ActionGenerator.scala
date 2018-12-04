object ActionGenerator {

  def apply(i: Int, lastIndex: Int): String = {
    // R1, R2, R3, ...
    val higherKindTypes = (1 to i).map(j => s"R$j").mkString(", ")
    // R1[_], R2[_], R3[_], ...
    val existentialTypes = (1 to i).map(j => s"R$j[_]").mkString(", ")
    // R1[A], R2[A], R3[A], ...
    val concreteTypes = (1 to i).map(j => s"R$j[A]").mkString(", ")
    // R1[AnyContent], R2[AnyContent], R3[AnyContent], ...
    val anyTypes = (1 to i).map(j => s"R$j[AnyContent]").mkString(", ")

s"""
package jp.t2v.lab.play2.actzip

import play.api.mvc.{Result, Request, ActionBuilder, AnyContent, Action, BodyParser}
import play.api.mvc.BodyParsers._
import scala.concurrent.{Future, ExecutionContext}

class ZippedAction$i[$existentialTypes, B](${(1 to i).map(j => s"val b$j: ActionBuilder[R$j, B]").mkString(", ")})(implicit ec: ExecutionContext) extends ActionBuilder[({type L[A] = ($concreteTypes)})#L, B] {

  override def parser: BodyParser[B] = b1.parser
  override protected def executionContext: ExecutionContext = ec

  override def invokeBlock[A](request: Request[A], block: (($concreteTypes)) => Future[Result]): Future[Result] = {
${(1 to i).map(j => s"    b$j.invokeBlock(request, { r$j: R$j[A] =>").mkString("\n")}
      block((${(1 to i).map(j => s"r$j").mkString(", ")}))
${(1 to i).map(j => s"    })").mkString("\n")}
  }

${if (i < lastIndex) {
  val n = i + 1
  s"  def zip[R$n[_]](b$n: ActionBuilder[R$n, B]): ZippedAction$n[$higherKindTypes, R$n, B] = new ZippedAction$n(${(1 to i).map(j => s"b$j").mkString(", ")}, b$n)"
} else ""}

${if (i < lastIndex - 1) {
  (2 to (lastIndex - i)).map { zi =>
    val kk = ((i+1) to (i + zi)).map(j => s"R$j[_]").mkString(", ")
    val k = ((i+1) to (i + zi)).map(j => s"R$j").mkString(", ")
    val n = i + zi
    s"  def zip[$kk](z: ZippedAction$zi[$k, B]): ZippedAction$n[$higherKindTypes, $k, B] = new ZippedAction$n(${(1 to i).map(j => s"b$j").mkString(", ")}, ${(1 to zi).map(j => s"z.b$j").mkString(", ")})"
  }.mkString("\n")
} else "" }

  def any(f: (($anyTypes)) => Result): Action[AnyContent] = apply(parse.anyContent)(f)
  def anyAsync(f: (($anyTypes)) => Future[Result]): Action[AnyContent] = async(parse.anyContent)(f)

}
"""
  }

}
