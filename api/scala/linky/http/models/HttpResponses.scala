package linky.http.models


object HttpResponses {
  val internalServerError: LinkyHttpResponse = LinkyHttpResponse(500, "Internal server error")
  val registeredUser: LinkyHttpResponse = LinkyHttpResponse(200, "User registered")
}
