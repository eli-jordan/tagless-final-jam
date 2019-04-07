name := "tagless-final-jam"
version := "0.1"

scalaVersion in ThisBuild := "2.12.8"
scalacOptions in ThisBuild += "-Ypartial-unification"

val CatsVersion       = "1.6.0"
val CatsEffectVersion = "1.2.0"
val CatsParVersion    = "0.2.1"
val ScalaTestVersion  = "3.0.6"

val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel"     %% "cats-core"   % CatsVersion,
    "org.typelevel"     %% "cats-effect" % CatsEffectVersion,
    "io.chrisdavenport" %% "cats-par"    % CatsParVersion,
    "org.scalatest"     %% "scalatest"   % ScalaTestVersion % "test"
  ),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
)

lazy val `arithmetic` = (project in file("arithmetic"))
  .settings(commonSettings)

lazy val `reading-list` = (project in file("reading-list"))
  .settings(commonSettings)
