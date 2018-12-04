package controllers

import play.api.mvc._
import jp.t2v.lab.play2.actzip._

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.{Inject, Singleton}

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private implicit val ec: ExecutionContext = cc.executionContext

  val AuthAction = Action andThen AuthFunction(cc)
  val DBTxAction = Action andThen TxFunction(cc)

  val MyAction = AuthAction zip DBTxAction

  val Foo = Action zip MyAction

  val Bar = MyAction zip Foo

  def index = Bar.any { case (r1, r2, _, _, _) =>
    println(r1.user)
    println(r2.dbSession)
    Ok(views.html.index("Your new application is ready."))
  }

  def index2 = Bar.anyAsync { case (r1, r2, _, _, _) =>
    println(r1.user)
    println(r2.dbSession)
    Future.successful(Ok(views.html.index("Your new application is ready.")))
  }

  def index3 = Bar.async(parse.json) { case (r1, r2, _, _, _) =>
    println(r1.user)
    println(r2.dbSession)
    Future.successful(Ok(views.html.index("Your new application is ready.")))
  }

}

case class User(value: String)
case class AuthRequest[A](user: User, private val request: Request[A]) extends WrappedRequest[A](request)

case class AuthFunction(cc: ControllerComponents) extends ActionFunction[Request, AuthRequest] {
  override def invokeBlock[A](request: Request[A], block: AuthRequest[A] => Future[Result]): Future[Result] = {
    val user = User(request.headers.get("user") getOrElse "GuestUser")
    block(AuthRequest(user, request))
  }
  override protected def executionContext: ExecutionContext = cc.executionContext
}

case class DBSession()
case class TxRequest[A](dbSession: DBSession, private val request: Request[A]) extends WrappedRequest[A](request)

case class TxFunction(cc: ControllerComponents) extends ActionFunction[Request, TxRequest] {
  override def invokeBlock[A](request: Request[A], block: TxRequest[A] => Future[Result]): Future[Result] = {
    block(TxRequest(DBSession(), request))
  }

  override protected def executionContext: ExecutionContext = cc.executionContext
}