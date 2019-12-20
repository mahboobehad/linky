package linky.http.router

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.{Directives, Route}
import linky.db.{Account, AccountsDAO}
import linky.http.models.{HttpResponses, JsonSupport, RegisterEntity}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class LinkyRouter(implicit val executionContext: ExecutionContext, implicit val logger: LoggingAdapter) extends Directives
  with JsonSupport
  with Authenticator {

  val route: Route =
    path("register") {
      post {
        entity(as[RegisterEntity]) { register =>
          onComplete {
            AccountsDAO.create(Account(register.username, register.email, register.password))
          } {
            case Success(_) => complete(HttpResponses.registeredUser)
            case Failure(e) =>
              logger.error(s"${e}")
              complete(HttpResponses.internalServerError)
          }
        }
      }
    } ~ path("shorten") {
      post {
        authenticateBasic(realm = "secure site", authenticate) { userId =>
          complete(s"The user is '$userId'")
        }
      }
    }
}