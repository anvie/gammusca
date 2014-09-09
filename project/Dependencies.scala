import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "typesafe repo"   at "http://repo.typesafe.com/typesafe/releases/",
    "glassfish repo"  at "http://download.java.net/maven/glassfish/",
    "spray repo"      at "http://repo.spray.cc/",
    "Ansvia repo release" at "http://scala.repo.ansvia.com/releases/",
    "Ansvia repo snapshot" at "http://scala.repo.ansvia.com/nexus/content/repositories/snapshots/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  val ansviaCommons = "com.ansvia" % "ansvia-commons" % "0.1.9"
  val specs2        = "org.specs2" %%  "specs2" % "2.4"
  val logback       = "ch.qos.logback" % "logback-classic" % "1.0.13"
  lazy val scalaRedis = "net.debasishg" %% "redisclient" % "2.12"
}
