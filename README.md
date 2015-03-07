# ActionZipper

Play2 ActionBuilder Composition Support

```scala
package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthActionBuilders
import scalikejdbc.DBActionBuilders
import jp.t2v.lab.play2.actzip._

object Application extends Controller with AuthConfigImpl with AuthActionBuilders with DBActionBuilders {

  val MyAction = AuthenticationAction zip DBTxAction

  def index = MyAction(parse.anyContent) { case Z(r1, r2) =>
    println(r1.user)
    println(r2.dbSession)
    Ok(views.html.index("Your new application is ready."))
  }

}
```


