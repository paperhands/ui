val _scalaVersion = "2.13.5"
val sttpVersion = "3.1.7"
val circeVersion = "0.14.0-M4"
val catsVersion = "2.4.2"
val catsEffVersion = "2.3.3"
val scalaJSDomVersion = "1.1.0"
val diodeVersion = "1.1.14"
val scalaReactVersion = "1.7.7"
val monocleVersion = "3.0.0-M3"
val minitestVersion = "2.9.3"
val reactVersion = "17.0.0"
val echartsVersion = "5.0.2"

ThisBuild / scalaVersion     := _scalaVersion
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "app.paperhands"
ThisBuild / organizationName := "paperhands"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSBundlerPlugin, ScalablyTypedConverterPlugin)
  .settings(
    name := "paperhands-ui",

    testFrameworks += new TestFramework("minitest.runner.Framework"),

    libraryDependencies ++= Seq(
      /* "io.circe"  %% "circe-core"     % circeVersion, */
      /* "io.circe"  %% "circe-generic"  % circeVersion, */
      /* "io.circe"  %% "circe-parser"   % circeVersion, */
      /* "io.circe"  %% "circe-literal"  % circeVersion, */
      /* "io.circe"  %% "circe-jawn"     % circeVersion, */

      /* "com.softwaremill.sttp.client3" %%% "core" % sttpVersion, */
      /* "com.softwaremill.sttp.client3" %%% "cats" % sttpVersion, */

      "org.scala-js" %%% "scalajs-dom" % scalaJSDomVersion,

      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-effect" % catsEffVersion,

      "io.suzaku" %%% "diode" % diodeVersion,
      "io.suzaku" %%% "diode-react" % diodeVersion,

      "com.github.japgolly.scalajs-react" %%% "core" % scalaReactVersion,
      "com.github.japgolly.scalajs-react" %%% "extra" % scalaReactVersion,
      "com.github.japgolly.scalajs-react" %%% "ext-cats" % scalaReactVersion,
      "com.github.japgolly.scalajs-react" %%% "ext-monocle-cats" % scalaReactVersion,

       "io.monix" %% "minitest" % minitestVersion % "test",
       "io.monix" %% "minitest-laws" % minitestVersion % "test",
       "com.codecommit" %% "cats-effect-testing-minitest" % "0.5.2" % "test",

       "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
       "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion, // Not required for Scala 3
    ),

    useYarn := true,
    stFlavour := Flavour.Japgolly,
    stIgnore := List("react", "react-dom"),

    npmDependencies in Compile ++= Seq(
      "react"          -> reactVersion,
      "react-dom"      -> reactVersion,
      "echarts"        -> echartsVersion,
      "@types/echarts" -> "4.9.6",
    ),

    scalaJSUseMainModuleInitializer := true,
    Global / onChangedBuildSource := ReloadOnSourceChanges,
  )
