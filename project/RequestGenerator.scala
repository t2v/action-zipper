object RequestGenerator {

  def apply(): String = {
s"""
package jp.t2v.lab.play2.actzip

import play.api.mvc.{Request, WrappedRequest}

${ (2 to 21).map { i =>
s"case class ZippedRequest$i[A, ${(1 to i).map(j => s"R$j[_]").mkString(", ")}](${(1 to i).map(j => s"_${j}: R$j[A]").mkString(", ")}, private val request: Request[A]) extends WrappedRequest[A](request)"
}.mkString("\n")}


object ZippedRequest {
${ (2 to 21).map { i =>
s"  def unapply[A, ${(1 to i).map(j => s"R$j[_]").mkString(", ")}](r: ZippedRequest$i[A, ${(1 to i).map(j => s"R$j").mkString(", ")}]): Option[(${(1 to i).map(j => s"R$j[A]").mkString(", ")})] = Some((${(1 to i).map(j => s"r._$j").mkString(", ")}))"
}.mkString("\n")}
}

"""
  }
}