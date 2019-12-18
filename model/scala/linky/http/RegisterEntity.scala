package linky.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class RegisterEntity(userName: String, password: String)

trait RegisterEntityJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val regFormats: RootJsonFormat[RegisterEntity] = jsonFormat2(RegisterEntity)
}