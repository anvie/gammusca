import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "typesafe repo"   at "http://repo.typesafe.com/typesafe/releases/",
    "glassfish repo"  at "http://download.java.net/maven/glassfish/",
    "spray repo"      at "http://repo.spray.cc/",
    "Ansvia repo" at "http://scala.repo.ansvia.com/releases/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  val ansviaCommons = "com.ansvia" % "ansvia-commons" % "0.0.8"
  val specs2        = "org.specs2" %%  "specs2" % "1.12.4"
  val logback       = "ch.qos.logback" % "logback-classic" % "1.0.13"
  lazy val scalaRedis = "net.debasishg" % "redisclient_2.9.1" % "2.9"
}
