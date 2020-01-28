package linky.http.router

import akka.http.scaladsl.server.directives.Credentials
import linky.db.{Account, AccountsDAO}

import scala.concurrent.Future

import scala.util.matching.Regex


trait Authenticator {
  this: LinkyRouter =>

  private val emailRegex: Regex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  def authenticate(credentials: Credentials): Future[Option[String]] =
    credentials match {
      case p@Credentials.Provided(id) => id match {
        case emailRegex(id) => validateWithDatabase(p, id, AccountsDAO.findByEmail)
        case _ => validateWithDatabase(p, id, AccountsDAO.findByUsername)
      }
      case _ => Future.successful(None)
    }

  private def validateWithDatabase(p: Credentials.Provided, id: String, validator: String => Future[Option[Account]]): Future[Option[String]] = {
    for {
      accountOpt <- validator(id)
      isValid = if (accountOpt.isDefined) p.verify(accountOpt.get.password) else false
      userId = if (isValid) Some(id) else None
    } yield userId
  }

}
