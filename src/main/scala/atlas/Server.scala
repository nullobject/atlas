package atlas

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import java.util.UUID
import scala.concurrent.duration._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport
import spray.routing.{ExceptionHandler, HttpServiceActor}
import spray.util.LoggingContext
import scala.language.postfixOps

/**
 * The server wraps player actions in an intention and forwards them to the
 * game. When the game responds the server completes the request.
 */
class Server(game: ActorRef) extends Actor with HttpServiceActor with SprayJsonSupport {
  import JsonFormats._

  implicit val timeout = Timeout(5 seconds)

  implicit def exceptionHandler(implicit log: LoggingContext) = ExceptionHandler.fromPF {
    case e: Player.InvalidActionException => ctx =>
      log.warning("Request {} could not be handled normally", ctx.request)
      ctx.complete(UnprocessableEntity, e.getMessage)
  }

  def receive = runRoute {
    headerValueByName("X-Player") { player =>
      path("intentions") {
        post {
          entity(as[Player.Action]) { action =>
            val playerId = UUID.fromString(player)
            val playerRequest = Player.Request(playerId, action)
            val response = ask(game, playerRequest).mapTo[WorldView]
            complete(response)
          }
        }
      }
    }
  }
}
