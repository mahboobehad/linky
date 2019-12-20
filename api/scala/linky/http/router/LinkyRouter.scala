package linky.http.router

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.{Directives, Route}
import linky.db.{Account, AccountsDAO}
import linky.http.models.{HttpResponses, JsonSupport, RegisterEntity, ShortenEntity, ShorterUrlResponse}
import linky.logic.UrlShortener

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class LinkyRouter(implicit val executionContext: ExecutionContext, implicit val logger: LoggingAdapter) extends Directives
  with JsonSupport
  with Authenticator
  with UrlShortener {

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
        authenticateBasicAsync(realm = "secure site", authenticate) { userId =>
          entity(as[ShortenEntity]){ shortenEntity =>
            val shorterUrl = generateShorter(shortenEntity.url)
            complete(ShorterUrlResponse(shorterUrl))
          }
        }
      }
    }
}