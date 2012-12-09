package atlas

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import akka.zeromq._
import java.util.UUID
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.language.postfixOps

object Server {
  sealed trait Message
  case object Tick extends Message
}

/**
 * The server manages client connections. Clients may submit their requests
 * intentions to the server for each game "tick". When the server processes the
 * client intentions the responses (world views) are returned to the client.
 */
class Server(game: ActorRef) extends Actor with ActorLogging {
  import context.dispatcher
  import Server._

  implicit val timeout = Timeout(5 seconds)
  val router = ZeroMQExtension(context.system).newSocket(SocketType.Router, Listener(self), Bind("tcp://127.0.0.1:1235"), HighWatermark(1000))
  var clients: Map[Seq[Byte], Game.PlayerIntention] = Map.empty

  override def preStart {
    context.system.scheduler.schedule(0.1 second, 0.1 second, self, Tick)
  }

  override def postRestart(reason: Throwable) {
    // Don't call preStart, only schedule once.
  }

  def receive: Receive = {
    case Tick =>
      clients.map {
        case (address, playerIntention) =>
          val future = ask(game, playerIntention).mapTo[WorldView]
          future map { case worldView =>
            router ! ZMQMessage(List(Frame(address), Frame(Nil), Frame(worldView.serialize)))
          } onFailure { case e: World.InvalidOperationException =>
            router ! ZMQMessage(List(Frame(address), Frame(Nil), Frame(e.getMessage)))
          }
      }
      clients = Map.empty
      game ! Game.Tick

    case message: ZMQMessage =>
      val address = message.frames(0).payload
      val playerId = UUID.fromString(new String(address.toArray, "UTF-8"))
      val json = new String(message.frames(2).payload.toArray, "UTF-8")
      val intention = Game.Intention.deserialize(json)
      val playerIntention = Game.PlayerIntention(playerId, intention)

      // Store the client address to player intention mapping.
      clients += (address -> playerIntention)
  }
}
