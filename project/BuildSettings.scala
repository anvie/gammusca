import sbt._
import Keys._
//import com.github.siasia.WebPlugin._
//import ls.Plugin._
import com.ansvia.onedir.OneDirPlugin

object BuildSettings {

  lazy val basicSettings = seq(
    version               := "0.0.3-SNAPSHOT",
    homepage              := Some(new URL("http://ansvia.com")),
    organization          := "com.ansvia.gammu",
    organizationHomepage  := Some(new URL("http://ansvia.com")),
    description           := "",
    startYear             := Some(2012),
    licenses              := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalaVersion          := "2.10.4",
    resolvers             ++= Dependencies.resolutionRepos,
    scalacOptions         := Seq("-deprecation", "-encoding", "utf8"),
    description           := "Gammu SMS Daemon using Scala"
  )

  lazy val moduleSettings = basicSettings ++ seq(
    // scaladoc settings
    (scalacOptions in doc) <++= (name, version).map { (n, v) => Seq("-doc-title", n, "-doc-version", v) }//,

    // publishing
    //credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    //crossPaths := true,
    //publishMavenStyle := false
  ) ++ OneDirPlugin.onedirSettings

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )
  
  lazy val withPublishing = seq(
      publishTo <<= version { (v:String) =>
            val ansviaRepo = "http://scala.repo.ansvia.com/nexus"
            if(v.trim.endsWith("SNAPSHOT"))
                Some("snapshots" at ansviaRepo + "/content/repositories/snapshots")
            else
                Some("releases" at ansviaRepo + "/content/repositories/releases")
      },

      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials-ansvia"),

      publishArtifact in Test := false,

      publishMavenStyle := true,

      pomIncludeRepository := { _ => false },

      crossPaths := true,

      pomExtra := (
          <url>http://www.ansvia.com</url>
          <developers>
            <developer>
              <id>@robin</id>
              <name>robin</name>
              <url>http://www.ansvia.com</url>
            </developer>
          </developers>)
  )

}

