package com.example

import zio.ZIO
import zio.console._
import zio.Task
import zio.ZLayer
import zio.Has

object ZLayersPlayground extends zio.App {

    // Horizontal composition
    val userBackendLayer: ZLayer[Any, Nothing, UserDbEnv with UserEmailerEnv] = 
        (UserDb.live ++ UserEmailer.live)

    // Vertical composition
    val userSubscriptionLayer: ZLayer[Any, Nothing, UserSubscriptionEnv] = 
        userBackendLayer >>> UserSubscription.live

    val jack = User("Jack", "jack@home.org")
    val message = "Welcome to the world!"
    val subscribeJack = UserSubscription.subscribe(jack).provideLayer(userSubscriptionLayer)
    
    override def run(args: List[String]) = 
        subscribeJack.exitCode
}
