// In the name of God
package linky.db

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import slick.dbio.DBIO


case class Account(username: String, email: String, password: String)

class Accounts(tag: Tag) extends Table[Account](tag, "accounts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def username = column[String]("username")

  def email = column[String]("email")

  def password = column[String]("password")

  def * = (username, email, password) <> (Account.tupled, Account.unapply)
}

object AccountsDAO extends TableQuery(new Accounts(_)) {
  val db = Database.forConfig("database")

  def findById(id: Long): Future[Option[Account]] = {
    db.run(this.filter(_.id === id).result).map(_.headOption)
  }

  def findByUsername(username: String): Future[Option[Account]] = {
    print(username)
    db.run(this.filter(_.username === username).result).map(_.headOption)
  }

  def findByEmail(email: String): Future[Option[Account]] = {
    db.run(this.filter(_.email === email).result).map(_.headOption)
  }

  def create(account: Account): Future[Int] = {
    db.run(this += account)

  }

  def deleteById(id: Long): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }

}
