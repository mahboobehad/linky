package linky.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.directives.Credentials

import scala.concurrent.{ExecutionContext, Future}

class LinkyRouter(implicit executionContext: ExecutionContext) extends Directives with RegisterEntityJsonSupport {

  private def authenticate(credentials: Credentials): Future[Option[String]] =
    credentials match {
      case p@Credentials.Provided(id) =>
        Future {
          if (p.verify("p4ssw0rd")) Some(id)
          else None
        }
      case _ => Future.successful(None)
    }

  val route: Route =
  path("register"){
      post{
        entity(as[RegisterEntity]) { register =>
          complete(s"username: ${register.userName} - password: ${register.password}")
        }
      }
    }
}