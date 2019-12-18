// In the name of God
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import linky.http.LinkyRouter

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  implicit val config: Config = ConfigFactory.load("server")
  val port = config.getInt("server.port")
  val host = config.getString("server.host")

  implicit val system: ActorSystem = ActorSystem("linky-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val bindingFuture = Http().bindAndHandle(new LinkyRouter().route, host, port)

}

