package linky.http.models


object HttpResponses {
  val internalServerError: HttpResponse = HttpResponse(500, "Internal server error")
  val registeredUser: HttpResponse = HttpResponse(200, "User registered")
}
