# ActionZipper [![Build Status](https://travis-ci.org/t2v/action-zipper.svg?branch=feature%2Ftravis)](https://travis-ci.org/t2v/action-zipper)

Play2 ActionBuilder Composition Support

```scala
package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthActionBuilders
import scalikejdbc.DBActionBuilders
import jp.t2v.lab.play2.actzip._

object Application extends Controller with AuthConfigImpl with AuthActionBuilders with DBActionBuilders {

  val MyAction = AuthenticationAction zip DBTxAction

  def index = MyAction(parse.json) { case (authRequest, dbRequest) =>
    println(authRequest.user)
    println(dbRequest.dbSession)
    Ok(views.html.index("Your new application is ready."))
  }

}
```

## Motivation

`ActionBuilder` is Play2 standard action composition system. 

It can compose other [ActionFunction](https://www.playframework.com/documentation/2.3.x/ScalaActionsComposition#Different-request-types)s that have same Request type.

However, ActionBuilders that have different request type can not compose each other. (for example, `AuthenticationAction` and `DBAction`)

Action-Zipper provides the way that make any `ActionBuilder`s enable to compose.


## Installation

Add dependency declarations into your `Build.scala` or `build.sbt` file:

```scala
libraryDependencies += "jp.t2v" %% "action-zipper" % "0.1.0"
```


## Alias

### any and anyAsync

Since `ActionBuilder#apply` and `ActionBuilder#async` are overloaded, we can not use `Pattern Matching Anonymous Functions`.

```scala
// compile error!!
def index = MyAction { case (authRequest, dbRequest) =>
```

So ZippedActionN has `any` and `anyAsync` method that can use instead of `apply` and `async`

```scala
def index = MyAction.any { case (authRequest, dbRequest) =>
```

```scala
def index = MyAction.anyAsync { case (authRequest, dbRequest) =>
```

## More Example

```scala
package controllers

import play.api.mvc._
import jp.t2v.lab.play2.actzip._

object Application extends Controller  {

  // it can chain more than 2
  val Action3 = Action zip Action zip Action
  
  val Action4 = Action zip Action zip Action zip Action
  
  // ZippedAction can zip another ZipedAction
  val Action7 = Action3 zip Action4

  def index = Action7.any { case (_, _, _, _, _, _, _) =>
    Ok(views.html.index("7 action are zipped"))
  }

}
```
