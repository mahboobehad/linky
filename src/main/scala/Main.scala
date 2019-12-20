// In the name of God

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import linky.http.router.LinkyRouter

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Main extends App{
  implicit val config: Config = ConfigFactory.load("server")
  val port: Int = config.getInt("server.port")
  val host: String = config.getString("server.host")

  implicit val system: ActorSystem = ActorSystem("linky-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val logger: LoggingAdapter = Logging(system, Main.getClass)

  val serverFuture: Future[Http.ServerBinding] = Http().bindAndHandle(new LinkyRouter().route, host, port)
  serverFuture.onComplete {
    case Success(bound) =>
      logger.info("Server launched at http://{}:{}/",
        bound.localAddress.getHostString,
        bound.localAddress.getPort)
    case Failure(e) =>
      logger.error("Server could not start!")
      e.printStackTrace()
      system.terminate()
  }
  Await.result(system.whenTerminated, Duration.Inf)
}

