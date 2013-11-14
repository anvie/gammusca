import sbt._
import Keys._

// Author: Robin

object Build extends Build {
	import BuildSettings._
	import Dependencies._

  	// root
	lazy val root = Project("root", file("."))
		.aggregate(mainapp)
		.settings(basicSettings: _*)
		.settings(noPublishing: _*)
		
	// modules
//	lazy val examples = Project("examples", file("examples"))
//		.settings(moduleSettings: _*)
//		.settings(libraryDependencies ++=
//			compile(ansviaCommons) ++
//			test(specs2) ++
//			runtime(logback)
//		)

	lazy val mainapp = Project("gammusca", file("gammusca"))
		.settings(moduleSettings: _*)
        .settings(withPublishing: _*)
		.settings(libraryDependencies ++=
			compile(ansviaCommons, scalaRedis) ++
			test(specs2) ++
			runtime(logback)
		)
}
