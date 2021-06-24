package com.example

import zio.ZIO
import zio.Task
import zio.ZLayer
import zio.Has

type UserSubscriptionEnv = Has[UserSubscription.Service]

object UserSubscription {
    //Service Definition
    class Service(notifier: UserEmailer.Service, userDb: UserDb.Service) {
        def subscribe(user: User): Task[User] =
            for {
                _ <- userDb.insert(user)
                _ <- notifier.notify(user, s"Welcome to the world, ${user.name}! We have some nice ZIO content for you.")
            } yield user
    }
    //Service Implementation
    val live = ZLayer.fromServices[UserEmailer.Service, UserDb.Service, UserSubscription.Service] {
        (userEmailer, userDb) => new Service(userEmailer, userDb)
    }
    //Service Front-facing API
    def subscribe(user: User): ZIO[UserSubscriptionEnv, Throwable, User] =
        ZIO.accessM(_.get.subscribe(user))
}
