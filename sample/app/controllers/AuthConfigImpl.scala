package controllers

import jp.t2v.lab.play2.auth.AuthConfig
import scala.reflect.{ClassTag, classTag}
import scala.concurrent.{Future, ExecutionContext}
import play.api.mvc.{Result, RequestHeader}

trait AuthConfigImpl extends AuthConfig {

  type Id = Long
  type Authority = Unit
  type User = String

  implicit def idTag: ClassTag[Id] = classTag[Id]

  def authorize(user: User, authority: Authority)(implicit context: ExecutionContext): Future[Boolean] = ???

  def authorizationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = ???

  def authenticationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = ???

  def logoutSucceeded(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = ???

  def loginSucceeded(request: RequestHeader)(implicit context: ExecutionContext): Future[Result] = ???

  def resolveUser(id: Id)(implicit context: ExecutionContext): Future[Option[User]] = ???

  def sessionTimeoutInSeconds: Int = ???

}
