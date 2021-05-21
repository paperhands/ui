val _scalaVersion = "2.13.5"
val scalaJSDomVersion = "1.1.0"
val diodeVersion = "1.1.14"
val scalaReactVersion = "1.7.7"
val monocleVersion = "3.0.0-M6"
val minitestVersion = "2.9.6"
val reactVersion = "17.0.0"
val echartsVersion = "5.0.2"

ThisBuild / scalaVersion := _scalaVersion
ThisBuild / version := "0.1.0"
ThisBuild / organization := "app.paperhands"
ThisBuild / organizationName := "paperhands"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "paperhands-ui",
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    scalacOptions ++= List(
      "-Ymacro-annotations",
      "-Yrangepos",
      "-Wunused:imports"
    ),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,
      "io.suzaku" %%% "diode" % diodeVersion,
      "io.suzaku" %%% "diode-react" % diodeVersion,
      "com.github.japgolly.scalajs-react" %%% "core" % scalaReactVersion,
      "com.github.japgolly.scalajs-react" %%% "extra" % scalaReactVersion,
      "io.monix" %%% "minitest" % minitestVersion % "test",
      "io.monix" %%% "minitest-laws" % minitestVersion % "test",
      "com.github.julien-truffaut" %%% "monocle-core" % monocleVersion,
      "com.github.julien-truffaut" %%% "monocle-macro" % monocleVersion // Not required for Scala 3
    ),
    useYarn := true,
    Compile / npmDependencies ++= Seq(
      "react" -> reactVersion,
      "react-dom" -> reactVersion,
      "echarts" -> echartsVersion,
      "dateformat" -> "4.5.1"
    ),
    scalaJSUseMainModuleInitializer := true,
    Global / onChangedBuildSource := ReloadOnSourceChanges
  )

addCommandAlias(
  "organizeImports",
  "scalafix dependency:OrganizeImports@com.github.liancheng:organize-imports:0.5.0"
)
addCommandAlias("cleanImports", "scalafix RemoveUnused")
