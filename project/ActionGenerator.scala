object ActionGenerator {

  def apply(i: Int, lastIndex: Int): String = {
    // R1, R2, R3, ...
    val oneKindTypes = (1 to i).map(j => s"R$j").mkString(", ")

s"""
package jp.t2v.lab.play2.actzip

import play.api.mvc.{Result, Request, ActionBuilder}
import scala.concurrent.Future

class ZippedAction$i[${(1 to i).map(j => s"R$j[_]").mkString(", ")}](${(1 to i).map(j => s"val b$j: ActionBuilder[R$j]").mkString(", ")}) extends ActionBuilder[({type L[A] = ZippedRequest$i[A, $oneKindTypes]})#L] {

  override def invokeBlock[A](request: Request[A], block: ZippedRequest$i[A, $oneKindTypes] => Future[Result]): Future[Result] = {
${(1 to i).map(j => s"    b$j.invokeBlock(request, { r$j: R$j[A] =>").mkString("\n")}
      block(new ZippedRequest$i[A, $oneKindTypes](${(1 to i).map(j => s"r$j").mkString(", ")}, request))
${(1 to i).map(j => s"    })").mkString("\n")}
  }

${if (i < lastIndex) {
  val n = i + 1
  s"  def zip[R$n[_]](b$n: ActionBuilder[R$n]): ZippedAction$n[$oneKindTypes, R$n] = new ZippedAction$n(${(1 to i).map(j => s"b$j").mkString(", ")}, b$n)"
} else ""}

${if (i < lastIndex - 1) {
  (2 to (lastIndex - i)).map { zi =>

  val kk = ((i+1) to (i + zi)).map(j => s"R$j[_]").mkString(", ")
  val k = ((i+1) to (i + zi)).map(j => s"R$j").mkString(", ")
  val n = i + zi

  s"  def zip[$kk](z: ZippedAction$zi[$k]): ZippedAction$n[$oneKindTypes, $k] = new ZippedAction$n(${(1 to i).map(j => s"b$j").mkString(", ")}, ${(1 to zi).map(j => s"z.b$j").mkString(", ")})"

  }.mkString("\n")
} else "" }

}
"""
  }

}
