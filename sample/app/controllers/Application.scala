package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthActionBuilders
import scalikejdbc.DBActionBuilders
import jp.t2v.lab.play2.actzip._
import scala.concurrent.Future

object Application extends Controller with AuthConfigImpl with AuthActionBuilders with DBActionBuilders {

  val MyAction = OptionalAuthAction zip DBTxAction

//  can not compile?
//  val MyAction2 = OptionalAuthAction zip DBTxAction zip Action
//  val Foo = Action zip MyAction

  def index = MyAction(parse.anyContent) { case (r1, r2, _) =>
    println(r1.user)
    println(r2.dbSession)
    Ok(views.html.index("Your new application is ready."))
  }

}