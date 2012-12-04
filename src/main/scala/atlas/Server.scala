package atlas

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import akka.zeromq._
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

  val router = ZeroMQExtension(context.system).newSocket(SocketType.Router, Listener(self), Bind("tcp://127.0.0.1:1235"), HighWatermark(1000))
  var clients: Map[Seq[Byte], Game.Intention] = Map.empty
  implicit val timeout = Timeout(5 seconds)

  override def preStart {
    context.system.scheduler.schedule(1 second, 1 second, self, Tick)
  }

  override def postRestart(reason: Throwable) {
    // Don't call preStart, only schedule once.
  }

  def receive: Receive = {
    case Tick =>
      processClientIntentions
      game ! Game.Tick

    case message: ZMQMessage =>
      log.debug(s"Received: $message")

      val address = message.frames(0).payload
      val json = new String(message.frames(2).payload.toArray, "UTF-8")
      val intention = Game.Intention.deserialize(json)

      // Store the client address to intention mapping.
      clients += (address -> intention)
  }

  def processClientIntentions = clients.map {
    case (address, intention) =>
      log.debug(s"Intention: $intention")
      val future = ask(game, intention).mapTo[WorldView]
      future.map { worldView =>
        log.debug(s"Result: $worldView")
        router ! ZMQMessage(List(Frame(address), Frame(Nil), Frame(worldView.serialize)))
      }
  }
}
