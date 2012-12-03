package atlas

import akka.actor.{Actor, ActorLogging}
import akka.zeromq._

object Router {
  sealed trait Message
  case object Tick extends Message
}

/**
 * The router manages client connections. Clients may submit their requests
 * intentions to the router for each game "tick". When the router processes the
 * client intentions the responses (world views) are returned to the client.
 */
class Router extends Actor with ActorLogging {
  import Router._

  val socket = ZeroMQExtension(context.system).newSocket(SocketType.Router, Listener(self), Bind("tcp://127.0.0.1:1235"), HighWatermark(1000))
  val organism1 = Organism()
  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  var worldView = WorldView(organisms = Set(organism1), cells = Set(cell1))
  var clients: Map[Seq[Byte], Intention] = Map.empty

  def receive: Receive = {
    case Tick =>
      // TODO: apply all intentions.
      // TODO: send each waiting player their world view.
      clients.map {
        case (address, intention) =>
          socket ! ZMQMessage(List(Frame(address), Frame(Nil), Frame(worldView.serialize)))
      }

    case message: ZMQMessage =>
      log.debug("Received: " + message)

      val address = message.frames(0).payload
      val json = new String(message.frames(2).payload.toArray, "UTF-8")
      val intention = Intention.deserialize(json)

      // Store the address/intention mapping.
      clients += (address -> intention)
  }
}
