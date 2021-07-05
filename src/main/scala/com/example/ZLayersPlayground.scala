package com.example

import zio.ZIO
import zio.console._
import zio.Task
import zio.ZLayer
import zio.Has

object ZLayersPlayground extends zio.App {

    // Horizontal composition - combining UserDb and Emailer Services
    val userBackendLayer: ZLayer[Any, Nothing, UserDbEnv with UserEmailerEnv] = 
        (UserDb.live ++ UserEmailer.live)

    // Vertical composition - filling the dependency into the UserSubscription Service
    val userSubscriptionLayer: ZLayer[Any, Nothing, UserSubscriptionEnv] = 
        (UserDb.live ++ UserEmailer.live) >>> UserSubscription.live

    val jack = User("Jack", "jack@home.org.au")
    val message = "Welcome to the world!"
    val subscribeJack = UserSubscription.subscribe(jack).provideLayer(userSubscriptionLayer)
    
    override def run(args: List[String]) = 
        subscribeJack.exitCode
}
