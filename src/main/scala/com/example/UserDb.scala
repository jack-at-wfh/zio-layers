package com.example

import zio._

final case class User(name: String, email: String)
/* Define the UserDb Service interface */
trait UserDb {
  def insert(user: User): Task[Unit]
}
/* Provide accessor methods through companion object */
object UserDb {
  def live: URLayer[Console, UserDb] = {
    for {
      console <- ZIO.service[Console]
    } yield UserDbLive(console)
  }.toManagedWith(_.shutdown).toLayer
  // Added toManagedWith to assure that the shutdown method is called to release
  // Easy to provide a test or pre-prod/stage
  // definition to switch out services in the layer.
}
/* Implementation of the service interface - home for methods & data, parameter arguments, etc */
case class UserDbLive(console: Console) extends UserDb {
  def insert(user: User): Task[Unit] = console.printLine(s"[UserDb] Inserting user: \"${user.name}\" into database.")
  def shutdown: UIO[Unit] = for {
    _ <- console.printLine("[UserDb] Shutting down user database...").orDie
  } yield ()
}
