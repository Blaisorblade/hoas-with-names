//A sample SBT file for using macros (and macro-paradise).
//
//To use macros, we need to have separate projects.
val macros = project in file("macros")
val root = project in file(".") dependsOn macros

//resolvers += Resolver.sonatypeRepo("releases")

//We need to specify the next build settings for all projects. Hence, we use ThisBuild.
scalacOptions in ThisBuild ++= Seq("-language:experimental.macros", "-feature", "-deprecation", "-unchecked", "-language:implicits")

scalaVersion in ThisBuild := "2.10.3"

// Add Scala reflection library. This is is needed for all projects using reflection, macros or both.
libraryDependencies in ThisBuild += "org.scala-lang" % "scala-reflect" % scalaVersion.value
