package com.example

import zio.{Console, Task, UIO, URLayer, ZIO}

trait UserSubscription {
  def subscribe(user: User): Task[User]
}
type MyEnv = UserDb with UserEmailer

object UserSubscription {
  def live: URLayer[MyEnv, UserSubscription] = {
    for {
      dbase <- ZIO.service[UserDb]
      emailer <- ZIO.service[UserEmailer]
    } yield UserSubscriptionLive(emailer, dbase)
  }.toLayer
}
case class UserSubscriptionLive(notifier: UserEmailer, userDatabase: UserDb) extends UserSubscription {
  def subscribe(user: User): Task[User] = for {
    _ <- userDatabase.insert(user)
    _ <- notifier.notify(user, s"Welcome to ZIO2.0 - ZLayer, ${user.name}!")
  } yield user
}