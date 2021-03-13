import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "paperhands-ui",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0",
    npmDependencies in Compile += "echarts" -> "5.0.2",
    scalaJSUseMainModuleInitializer := true,
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
