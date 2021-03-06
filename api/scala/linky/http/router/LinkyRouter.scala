package linky.http.router

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.StatusCodes
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

  // TODO add metrics
  val route: Route =
    path("register") {
      post {
        entity(as[RegisterEntity]) { register =>
          onComplete {
            AccountsDAO.create(Account(register.username, register.email, register.password))
          } {
            case Success(_) => complete(HttpResponses.registeredUser)
            case Failure(e) =>
              logger.error(s"Failed to register -> ${e}")
              complete(HttpResponses.internalServerError)
          }
        }
      }
    }~
      path("shorten") {
      post {
        authenticateBasicAsync(realm = "secure site", authenticate) { userId =>
          entity(as[ShortenEntity]) { shortenEntity =>
            val shorterUrl = generateAndSaveShorterUrl(shortenEntity, userId)
            complete(ShorterUrlResponse(shorterUrl))
          }
        }
      }
    } ~
      path("""re.+""".r) { shorterUrl =>
      get {
        redirect(getUrl(shorterUrl), StatusCodes.TemporaryRedirect)
      }
    }
}

