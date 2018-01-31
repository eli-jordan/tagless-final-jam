
name := "tagless-final-jam-2018"
version := "0.1"

scalaVersion in ThisBuild := "2.12.3"
scalacOptions in ThisBuild += "-Ypartial-unification"

resolvers += Resolver.sonatypeRepo("releases")

val commonSettings = Seq(
    libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-core" % "1.0.1",
        "org.typelevel" %% "cats-effect" % "0.8",
        "org.scalatest" %% "scalatest" % "3.0.4" % "test",
        "io.monix" %% "monix" % "3.0.0-M3",
        "com.typesafe.play" %% "play-json" % "2.6.8"
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
)

lazy val part1 = (project in file("part1")).settings(commonSettings)

lazy val `part1-arithmetic` =
    (project in file("part1-arithmetic")).settings(commonSettings)

lazy val `part2a-domain-model` =
    (project in file("part2a-domain-model")).settings(commonSettings)

lazy val `part2b-composing-algebras` =
    (project in file("part2b-composing-algebras")).settings(commonSettings)

lazy val `part2c-error-handling` =
    (project in file("part2c-error-handling")).settings(commonSettings)

lazy val `part2d-parallelism` =
    (project in file("part2d-parallelism")).settings(commonSettings)

lazy val `part2e-real-repository` =
    (project in file("part2e-real-repository")).settings(commonSettings)


// Answers
lazy val `part1-arithmetic-answer` =
    (project in file("answers/part1-arithmetic")).settings(commonSettings)

lazy val `part2a-domain-model-answer` =
    (project in file("answers/part2a-domain-model")).settings(commonSettings)

lazy val `part2b-composing-algebras-answer` =
    (project in file("answers/part2b-composing-algebras")).settings(commonSettings)

lazy val `part2c-error-handling-answer` =
    (project in file("answers/part2c-error-handling")).settings(commonSettings)

lazy val `part2d-parallelism-answer` =
    (project in file("answers/part2d-parallelism")).settings(commonSettings)

//lazy val `part2e-real-repository-answer` =
//    (project in file("answers/part2e-real-repository")).settings(commonSettings)
