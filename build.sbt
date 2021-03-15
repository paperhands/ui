val _scalaVersion = "2.13.5"
val sttpVersion = "3.1.7"
val circeVersion = "0.14.0-M4"
val catsVersion = "2.4.2"
val catsEffVersion = "2.3.3"
val scalaJSDomVersion = "1.1.0"

ThisBuild / scalaVersion     := _scalaVersion
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "app.paperhands"
ThisBuild / organizationName := "paperhands"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "paperhands-ui",

    testFrameworks += new TestFramework("minitest.runner.Framework"),

    libraryDependencies ++= Seq(
      "io.circe"  %% "circe-core"     % circeVersion,
      "io.circe"  %% "circe-generic"  % circeVersion,
      "io.circe"  %% "circe-parser"   % circeVersion,
      "io.circe"  %% "circe-literal"  % circeVersion,
      "io.circe"  %% "circe-jawn"     % circeVersion,

      "com.softwaremill.sttp.client3" %%% "core" % sttpVersion,
      "com.softwaremill.sttp.client3" %%% "cats" % sttpVersion,

      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-effect" % catsEffVersion,

      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,

       "io.monix" %% "minitest" % "2.9.3" % "test",
       "io.monix" %% "minitest-laws" % "2.9.3" % "test",
       "com.codecommit" %% "cats-effect-testing-minitest" % "0.5.2" % "test",
    ),

    npmDependencies in Compile += "echarts" -> "5.0.2",

    scalaJSUseMainModuleInitializer := true,
    Global / onChangedBuildSource := ReloadOnSourceChanges,
  )
