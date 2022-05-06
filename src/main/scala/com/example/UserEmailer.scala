package com.example

import zio.{Console, Task, UIO, URLayer, ZIO}

trait UserEmailer {
  def notify(user: User, message: String): Task[Unit]
}
object UserEmailer {
  def live: URLayer[Console, UserEmailer] = {
    for {
      console <- ZIO.service[Console]
    } yield UserEmailerLive(console)
  }.toManagedWith(_.releaseEmailer).toLayer
}
case class UserEmailerLive(console: Console) extends UserEmailer {
  def notify(user: User, message: String): Task[Unit] = console.printLine(s"[User Emailer] Sending \"$message\" to ${user.name} at ${user.email}.")
  def releaseEmailer: UIO[Unit] = for {
    _ <- console.printLine("Releasing user emailer resource...").orDie
  } yield ()
}