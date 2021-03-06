//A sample SBT file for using macros (and macro-paradise).
//
//To use macros, we need to have separate projects.
val macros = project in file("macros")
val root = project in file(".") dependsOn macros

//resolvers += Resolver.sonatypeRepo("releases")

//We need to specify the next build settings for all projects. Hence, we use ThisBuild.
scalacOptions in ThisBuild ++= Seq("-language:experimental.macros", "-feature", "-deprecation", "-unchecked", "-language:implicitConversions")

scalaVersion in ThisBuild := "2.11.2"

// Add Scala reflection library. This is is needed for all projects using reflection, macros or both.
libraryDependencies in ThisBuild += "org.scala-lang" % "scala-reflect" % scalaVersion.value
/*
//Useful for using toolboxes in testing during debugging.
//libraryDependencies in ThisBuild += "org.scala-lang" % "scala-compiler" % scalaVersion.value
// Add the macro-paradise compiler plugin: This is needed to use quasiquotes
// and other extended macro features.
libraryDependencies in ThisBuild += compilerPlugin("org.scalamacros" % "paradise" % "2.0.0-M3" cross CrossVersion.full)
//
libraryDependencies in ThisBuild += "org.scalamacros" % "quasiquotes" % "2.0.0-M3" cross CrossVersion.full
//Most documentation uses addCompilerPlugin(dependency), but that's equivalent to the following:
//  libraryDependencies += compilerPlugin(dependency)
//We instead use compilerPlugin to be able to specify the scope.
//Strictly speaking, this compiler plugin is only needed for the macros project; however, I chose to make it available everywhere for simplicity.
 */
