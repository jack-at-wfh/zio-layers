package com.example

import zio._

object logging {

  type Logging = Has[Logging.Service]

  object Logging {

    trait Service {
      def logLine(line: String): UIO[Unit]
    }

    final case class LoggingLive() extends Service {
      def logLine(line: String): UIO[Unit] =
        UIO.effectTotal(println(line))
    }

    final case class LoggingTest() extends Service {
      def logLine(line: String): UIO[Unit] =
        ???
    }

    def live: ZLayer[Any, Nothing, Logging] =
      ZLayer.succeed(LoggingLive())

    def test: ZLayer[Any, Nothing, Logging] =
      ZLayer.succeed(LoggingTest())

  }

  def logLine(line: String): ZIO[Logging, Nothing, Unit] =
    ZIO.accessM(_.get.logLine(line))
}