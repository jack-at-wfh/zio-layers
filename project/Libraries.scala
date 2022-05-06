import sbt._

object Versions {
  val zioVersion = "2.0.0-RC2"
}

object Libraries {
  import Versions._
  val zio = "dev.zio" %% "zio" % zioVersion
  val zioStreams = "dev.zio" %% "zio-streams" % zioVersion
  val zioTest = "dev.zio" %% "zio-test" % zioVersion
}
