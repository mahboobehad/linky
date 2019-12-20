package linky.http.router

import akka.http.scaladsl.server.directives.Credentials
import linky.db.{Account, AccountsDAO}

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.util.matching.Regex


trait Authenticator {
  this: LinkyRouter =>

  private val emailRegex: Regex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r



  def authenticate(credentials: Credentials) =
    credentials match {
      case p@Credentials.Provided(id) => id match {
        case emailRegex(id) => for {
          accountOpt <- AccountsDAO.findByEmail(id)
          isValid = p.verify(if (accountOpt.isDefined) accountOpt.get.password else "")
          userId = if(isValid) Some(id) else None
        }yield userId
        case _ => for{
          accountOpt <- AccountsDAO.findByEmail(id)
          isValid = p.verify(if (accountOpt.isDefined) accountOpt.get.password else "")
          userId = if(isValid) Some(id) else None

        }yield userId
      }
      case _ => Future.successful(None)
    }
}
