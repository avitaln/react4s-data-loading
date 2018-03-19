val scalaJsReactVersion = "latest.version"

enablePlugins(ScalaJSPlugin)

scalaJSUseMainModuleInitializer := true
scalaVersion := "2.12.4"
name := "example"

resolvers += Resolver.sonatypeRepo("snapshots")
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.4"
libraryDependencies += "com.github.ahnfelt" %%% "react4s" % "0.9.4-SNAPSHOT"
