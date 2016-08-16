package com.babylonhealth.check_base

import akka.actor.ActorSystem
import akka.testkit._
import akka.util.Timeout
import org.scalatest.{FreeSpec, Matchers}
import spray.testkit.ScalatestRouteTest

import scala.concurrent.duration._


class RestApiTest extends FreeSpec with Matchers with ScalatestRouteTest with Routing {
  def actorRefFactory = system
  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5.second.dilated(system))

  implicit val timeout = Timeout(5 seconds)

  "get-it" - {
    "should be non-empty" in {
//      Get(s"/get-it/1") ~> routes ~> check {
      }
    }
  }

}
