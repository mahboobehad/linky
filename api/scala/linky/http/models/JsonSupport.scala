package linky.http.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val registerEntityFormats: RootJsonFormat[RegisterEntity] = jsonFormat3(RegisterEntity)
  implicit val httpResponseFormats: RootJsonFormat[HttpResponse] = jsonFormat2(HttpResponse)
  implicit val shortenEntityFormats: RootJsonFormat[ShortenEntity] = jsonFormat2(ShortenEntity)
  implicit val shorterUrlFormats: RootJsonFormat[ShorterUrlResponse] = jsonFormat1(ShorterUrlResponse)
}
