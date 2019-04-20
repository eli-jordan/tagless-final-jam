name := "tagless-final-jam"
version := "0.1"

scalaVersion in ThisBuild := "2.12.8"
scalacOptions in ThisBuild += "-Ypartial-unification"

val CatsVersion       = "1.6.0"
val CatsEffectVersion = "1.2.0"
val CatsParVersion    = "0.2.1"
val ScalaTestVersion  = "3.0.6"
val Http4sVersion     = "0.20.0-RC1"
val CirceVersion      = "0.11.1"

val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel"     %% "cats-core"            % CatsVersion,
    "org.typelevel"     %% "cats-effect"          % CatsEffectVersion,
    "io.chrisdavenport" %% "cats-par"             % CatsParVersion,
    "org.http4s"        %% "http4s-circe"         % Http4sVersion,
    "org.http4s"        %% "http4s-dsl"           % Http4sVersion,
    "org.http4s"        %% "http4s-blaze-server"  % Http4sVersion,
    "org.http4s"        %% "http4s-blaze-client"  % Http4sVersion,
    "io.circe"          %% "circe-generic"        % CirceVersion,
    "io.circe"          %% "circe-parser"         % CirceVersion,
    "io.circe"          %% "circe-generic-extras" % CirceVersion,
    "ch.qos.logback"    % "logback-classic"       % "1.2.3",
    "org.scalatest"     %% "scalatest"            % ScalaTestVersion % "test"
  ),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
)

lazy val `arithmetic` = (project in file("arithmetic"))
  .settings(commonSettings)

lazy val `reading-list` = (project in file("reading-list"))
  .settings(commonSettings)
