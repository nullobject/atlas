package atlas

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import java.util.UUID
import scala.concurrent.duration._
import scala.language.postfixOps
import spray.routing.HttpServiceActor
import spray.httpx.SprayJsonSupport

/**
 * The server manages client connections. Clients may submit their requests
 * intentions to the server for each game "tick". When the server processes the
 * client intentions the responses (world views) are returned to the client.
 */
class Server(game: ActorRef) extends Actor with HttpServiceActor with SprayJsonSupport {
  import JsonFormats._

  implicit val timeout = Timeout(5 seconds)

  def receive = runRoute {
    headerValueByName("X-Player") { player =>
      path("intentions") {
        post {
          entity(as[World.Action]) { action =>
            val playerId = UUID.fromString(player)
            val playerIntention = Player.Intention(playerId, action)
            val response = ask(game, playerIntention).mapTo[WorldView]
            complete(response)
          }
        }
      }
    }
  }
}
