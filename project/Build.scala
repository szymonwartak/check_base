import sbt.Keys._
import sbt._
import spray.revolver.RevolverKeys

object BabylonBuild extends Build with RevolverKeys {

  val akkaV = "2.3.3"
  val sprayV = "1.3.2"
  val luceneV = "6.1.0"

  val coreSettings = Seq(
    parallelExecution in Test := false,
    name := "check_base",
    version := "1.0",
    scalaVersion := "2.11.8",
    javaOptions in reStart += "-Xmx2g",
    mainClass in reStart := Some("com.babylonhealth.check_base.RestApi"),
    resolvers ++= Seq(
      Resolver.mavenLocal
    ),
    libraryDependencies ++= Seq(
      "org.apache.lucene" % "lucene-core" % luceneV,
      "org.apache.lucene" % "lucene-analyzers-common" % luceneV,
      "org.apache.lucene" % "lucene-queryparser" % luceneV,
      "org.apache.lucene" % "lucene-memory" % luceneV,
      "com.google.code.gson" % "gson" % "2.7",
      "com.typesafe.akka" %% "akka-remote" % akkaV,
      "com.typesafe.akka" %% "akka-testkit" % akkaV,
      "org.scalatest" %% "scalatest" % "2.2.2" % "test",
      "io.spray" %% "spray-io" % sprayV,
      "io.spray" %% "spray-can" % sprayV,
      "io.spray" %% "spray-json" % sprayV,
      "io.spray" %% "spray-httpx" % sprayV,
      "io.spray" %% "spray-routing" % sprayV,
      "io.spray" %% "spray-testkit" % sprayV % "test"

    )
  )

  lazy val root = Project("check_base", file(".")).settings(
    coreSettings : _*
  )

}
