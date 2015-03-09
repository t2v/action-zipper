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

  def index = MyAction(parse.json) { case ZippedRequest(authRequest, dbRequest) =>
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
resolvers           += Resolver.sonatypeRepo("snapshots")
libraryDependencies += "jp.t2v" %% "action-zipper" % "0.1.0-SNAPSHOT"
```


## Alias

### `Z` extractor

`ZippedRequest` extractor is too long. so we define short name alias as `Z`

```scala
def index = MyAction(parse.json) { case Z(authRequest, dbRequest) =>
  println(authRequest.user)
  println(dbRequest.dbSession)
  Ok(views.html.index("Your new application is ready."))
}
```


### any and anyAsync

Since `ActionBuilder#apply` and `ActionBuilder#async` are overloaded, we can not use `Pattern Matching Anonymous Functions`.

```scala
// compile error!!
def index = MyAction { case Z(authRequest, dbRequest) =>
```

So ZippedActionN has `any` and `anyAsync` method that can use instead of `apply` and `async`

```scala
def index = MyAction.any { case Z(authRequest, dbRequest) =>
```

```scala
def index = MyAction.anyAsync { case Z(authRequest, dbRequest) =>
```

