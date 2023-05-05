
val Sl4jVersion = "1.7.36"
val CatsVersion = "2.9.0"
val CatsEffectVersion = "2.5.5"
val Log4CatsVersion = "1.7.0"

lazy val root = (project in file("."))
  .settings(
    organization := "psf", // PremiumSoftwareFactory
    name := "AsyncVsSync",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-simple" % Sl4jVersion,
      "org.typelevel" %% "cats-core" % CatsVersion,
      "org.typelevel" %% "cats-kernel" % CatsVersion,
      "org.typelevel" %% "cats-effect" % CatsEffectVersion,
      "org.typelevel" %% "log4cats-core" % Log4CatsVersion,
    )
  )


scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)