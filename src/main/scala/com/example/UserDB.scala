package com.example

import zio.ZIO
import zio.ZLayer
import zio.Task
import zio.Has

type UserDbEnv = Has[UserDb.Service]

object UserDb {
    //Service Definition
    trait Service {
        def insert(user: User): Task[Unit]
    }
    //Service Implementation
    val live: ZLayer[Any, Nothing, UserDbEnv] = ZLayer.succeed(new Service {
        override def insert(user: User) = Task {
            println(s"[User Database] insert into public.user value ('${user.email}')")
        }
    })
    //Service Front-facing API
    def insert(user: User): ZIO[UserDbEnv, Throwable, Unit] = 
        ZIO.accessM(_.get.insert(user))
}
