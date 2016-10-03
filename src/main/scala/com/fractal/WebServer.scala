package com.fractal

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes}
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import scala.io.StdIn

object WebServer {

  def boot(url:String, port:Int) {

    implicit val system = ActorSystem("actorSystem")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    println("booting...")

    val multiTrans: Route =
      path(Segment / "transactions" /) { userName =>
        get {
          complete {
            RequestHandler.getAllTransactions(userName)
          }
        } ~
        post {
          complete {
            RequestHandler.queueTransaction(userName)
          }
        } ~
        delete {
          complete {
            RequestHandler.deleteAllTransactions(userName)
          }
        } ~
        put {
          parameter('id.as[Int].*) { id =>
            complete {
              RequestHandler.updateAllTransactions(userName, id.toList.reverse)
            }
          }
        }
      }

    val singleTrans: Route =
      path(Segment / "transactions" / IntNumber) { (userName, transID) =>
        get {
          complete {
            RequestHandler.getTransaction(userName, transID)
          }
        } ~
        post {
          complete {
            RequestHandler.executeTransaction(userName, transID)
          }
        } ~
        delete {
          complete {
            RequestHandler.deleteTransaction(userName, transID)
          }
        } ~
        put {
          parameter('newID.as[Int]) { newID =>
            complete {
              RequestHandler.modifyTransaction(userName, transID, newID)
            }
          }
        }
      }

    val route: Route =
      multiTrans~
        singleTrans

    val bindingFuture = Http().bindAndHandle(route, url, port)

    bindingFuture.onFailure {
      case ex: Exception =>
        println("We have a problem: " + ex.toString)
    }

    println(s"Server online at $url:$port/\nPress enter to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ ⇒ system.terminate())
  }
}
