name := "tagless-final-jam"
version := "0.1"

scalaVersion in ThisBuild := "2.12.8"
scalacOptions in ThisBuild += "-Ypartial-unification"

//resolvers += Resolver.sonatypeRepo("releases")

val CatsVersion       = "1.6.0"
val CatsEffectVersion = "1.2.0"
val ScalaTestVersion  = "3.0.6"

val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core"   % CatsVersion,
    "org.typelevel" %% "cats-effect" % CatsEffectVersion,
    "org.scalatest" %% "scalatest"   % ScalaTestVersion % "test"
  ),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
)

lazy val `arithmetic` = (project in file("arithmetic"))
  .settings(commonSettings)
