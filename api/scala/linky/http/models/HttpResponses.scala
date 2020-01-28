package linky.http.models

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}


object HttpResponses {
  val internalServerError: LinkyHttpResponse = LinkyHttpResponse(500, "Internal server error")
  val registeredUser: LinkyHttpResponse = LinkyHttpResponse(200, "User registered")
  def expandedUrl(url: Option[String]): HttpResponse = HttpResponse(status = 200, entity = url match {
    case Some(value) => HttpEntity(ContentTypes.`text/html(UTF-8)`, value)
    case _ => HttpEntity.Empty
  }
  )
}
