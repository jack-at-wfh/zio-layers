package com.example

import zio.ZIO
import zio.Task
import zio.ZLayer
import zio.Has

type UserEmailerEnv = Has[UserEmailer.Service]

object UserEmailer {
    //Service Definition
    trait Service {
        def notify(user: User, message: String): Task[Unit]
    }
    //Service Implementation
    val live: ZLayer[Any, Nothing, UserEmailerEnv] = ZLayer.succeed(new Service {
        override def notify(user: User, message: String) = Task {
            println(s"[User Emailer] Sending '$message' to ${user.email}")
        }
    })
    //Service Front-facing API
    def notify(user: User, message: String): ZIO[UserEmailerEnv, Throwable, Unit] =
        ZIO.accessM(hasService => hasService.get.notify(user,message))
}
