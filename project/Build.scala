import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-ground"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "commons-io" % "commons-io" % "2.1",
    "net.htmlparser.jericho" % "jericho-html" % "3.3",
    "commons-codec" % "commons-codec" % "1.7",
    "net.sourceforge.cssparser" % "cssparser" % "0.9.9"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
