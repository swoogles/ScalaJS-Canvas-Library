name := "canvas-manipulation"

lazy val scalaV = "2.11.7"

lazy val commonSettings = Seq(
  organization := "com.billding",
  version := "0.1.0",
  scalaVersion := scalaV
)

lazy val client = 
  (project in file("."))
    .settings(commonSettings: _*)
    .settings(
      scalaVersion := scalaV,
      persistLauncher := true,
      persistLauncher in Test := false,
      libraryDependencies ++= Seq(
        "com.vmunier" % "play-scalajs-scripts_2.11" % "0.4.0",
        "org.scala-js" %%% "scalajs-dom" % "0.8.0",
        "com.lihaoyi" %%% "pprint" % "0.3.8",
        "com.lihaoyi" %% "utest" % "0.3.1",
        "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
      ),
      testFrameworks += new TestFramework("utest.runner.Framework")
    ).enablePlugins(ScalaJSPlugin, ScalaJSPlay)

lazy val shared = 
  (crossProject.crossType(CrossType.Pure) in file("shared"))
  // .settings(commonSettings: _*)
  .jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js
