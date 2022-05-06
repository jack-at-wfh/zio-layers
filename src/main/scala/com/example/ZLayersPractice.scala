package com.example

import zio.*

object ZLayersPractice extends ZIOAppDefault {
  val jack: User = User("Jack", "jack@home.org")

  def program(user: User): ZIO[UserSubscription, Throwable, Unit] = for {
    subs <- ZIO.service[UserSubscription]
    _ <- subs.subscribe(user)
  } yield ()

  val run: Task[Unit] =
    program(jack)
      .provide(
        UserSubscription.live,
        UserDb.live,
        UserEmailer.live,
        Console.live
      )
}
