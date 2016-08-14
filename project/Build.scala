import sbt.Keys._
import sbt._
import com.localytics.sbt.dynamodb.DynamoDBLocalKeys._
import spray.revolver.RevolverKeys

object BabylonBuild extends Build with RevolverKeys {

  val akkaV = "2.3.3"
  val sprayV = "1.3.2"

  val dynamoDbSettings = Seq(
    startDynamoDBLocal <<= startDynamoDBLocal.dependsOn(compile in Test),
    test in Test <<= (test in Test).dependsOn(startDynamoDBLocal),
    testOptions in Test <+= dynamoDBLocalTestCleanup
  )
  val coreSettings = Seq(
    parallelExecution in Test := false,
    name := "rest_dynamodb_mustachejs",
    version := "1.0",
    scalaVersion := "2.11.8",
    javaOptions in reStart += "-Xmx2g",
    mainClass in reStart := Some("com.archetype.rest_dynamodb.RestApi"),
    resolvers ++= Seq(
      Resolver.mavenLocal
    ),
    libraryDependencies ++= Seq(
      "com.github.seratch" %% "awscala" % "0.3.+",
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

  lazy val root = Project("rest_dynamodb", file(".")).settings(
    coreSettings ++ dynamoDbSettings: _*
  )

}
