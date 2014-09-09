
resolvers ++= Seq(
	"Ansvia repo" at "http://scala.repo.ansvia.com/releases"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

addSbtPlugin("com.ansvia" % "onedir" % "0.6")

//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.3")
