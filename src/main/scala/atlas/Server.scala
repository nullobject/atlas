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

  val router = ZeroMQExtension(context.system).newSocket(SocketType.Router, Listener(self), Bind("tcp://127.0.0.1:1235"), HighWatermark(1000))
  val organism1 = Organism()
  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  var worldView = WorldView(organisms = Set(organism1), cells = Set(cell1))
  var clients: Map[Seq[Byte], Intention] = Map.empty
  implicit val timeout = Timeout(5 seconds)

  def receive: Receive = {
    // Applies each client intention and replies with their world view.
    case Tick =>
      game ! Game.Tick
      clients.map {
        case (address, intention) =>
          val future = ask(game, Game.Move(UUID.fromString("0798acfd-0675-465b-966d-d77f044e443b"), Vector2(0, 0))).mapTo[World]
          future.map { result =>
            log.info("Result: " + result)
            router ! ZMQMessage(List(Frame(address), Frame(Nil), Frame(worldView.serialize)))
          }
      }

    case message: ZMQMessage =>
      log.debug("Received: " + message)

      val address = message.frames(0).payload
      val json = new String(message.frames(2).payload.toArray, "UTF-8")
      val intention = Intention.deserialize(json)

      // Store the client address to intention mapping.
      clients += (address -> intention)
  }
}
