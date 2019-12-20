package linky.http.router

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.sslconfig.ssl.ClientAuth
import com.typesafe.sslconfig.ssl.ClientAuth.None
import linky.db.{Account, AccountsDAO}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.util.matching.Regex


trait Authenticator {
  this: LinkyRouter =>

  private val emailRegex: Regex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  def getPassword(id: String) = id match {
    case emailRegex(id) => "pass"
    case _ => { print("other ")
      AccountsDAO.findByUsername(id).onComplete {
        case Success(Some(Account(_, _, _, registeredPassword))) => registeredPassword
        case Failure(exception) => ""
      }
      ""
    }

  }


  def authenticate(credentials: Credentials): Option[String] =
    credentials match {
      case p@Credentials.Provided(id) => if (p.verify(getPassword(id))) Some(id) else Some("")
      case _ => Some("")
    }
}
