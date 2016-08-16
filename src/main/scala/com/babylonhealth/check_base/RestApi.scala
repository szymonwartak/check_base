package com.babylonhealth.check_base

import akka.actor.ActorSystem
import akka.util.Timeout
import com.google.gson.{Gson, GsonBuilder}
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import spray.routing.{HttpService, SimpleRoutingApp}

import scala.concurrent.duration._
import collection.JavaConversions._

// request param classes and deserialisation implicits
case class Params2(p1: Int, p2: String)

object JsonDeser extends DefaultJsonProtocol {
  implicit val m1: RootJsonFormat[Params2] = jsonFormat2(Params2.apply)
}

object RestApi extends SimpleRoutingApp with Routing with App {

  implicit val system = ActorSystem("restapi")
  implicit val timeout = Timeout(5 seconds)

  startServer(interface = "0.0.0.0", port = 8080)(routes)
}

trait Routing extends HttpService with MetaToResponseMarshallers {

  import JsonDeser._

  val doit = new BusinessLogic

  lazy val routes = {
    pathPrefix("css") {
      get {
        getFromResourceDirectory("webapp/css")
      }
    } ~
      pathPrefix("js") {
        get {
          getFromResourceDirectory("webapp/js")
        }
      } ~
      path("") {
        getFromResource("webapp/ca_index.html")
      } ~
      path("do-it") {
        post {
          entity(as[Params2]) { du =>
            detach() {
              complete {
                "ok"
              }
            }
          }
        }
      } ~
      path("search" / RestPath) { query =>
        get {
          complete {
            val props = UserProps("en", "male")
            gson.toJson(doit.search(props)(query.toString))
          }
        }
      }
  }
  val gson: Gson = new GsonBuilder().setPrettyPrinting.create
  val businessLogic = new BusinessLogic
}
