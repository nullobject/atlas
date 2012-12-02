package atlas

import akka.actor.{Actor, FSM, Props}
import akka.zeromq._
import java.util.UUID

object Player {
  sealed trait State
  case object Idle extends State
  case object Error extends State

  sealed trait Message
  case object Tick extends Message
}

// The player FSM.
class Player extends Actor with FSM[Player.State, WorldView] {
  import Player._

  val socket = ZeroMQExtension(context.system).newSocket(SocketType.Req, Listener(self), Bind("tcp://127.0.0.1:1235"))
  val organism1 = Organism()
  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val worldView = WorldView(organisms = Set(organism1), cells = Set(cell1))

  startWith(Idle, worldView)
  when(Idle) {
    case Event(Tick, worldView) =>
      socket ! ZMQMessage(Seq(Frame(worldView.serialize)))
      stay
  }
  whenUnhandled {
    case Event(message: ZMQMessage, _) =>
      val intention = Intention.deserialize(message.firstFrameAsString)
      log.info("Received: " + intention)
      stay
    case Event(message, _) ⇒
      log.warning("Received unknown event: " + message)
      goto(Error)
  }
  initialize
}
