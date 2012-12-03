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

  val socket = ZeroMQExtension(context.system).newSocket(SocketType.Dealer, Listener(self), Bind("tcp://127.0.0.1:1235"))
  val organism1 = Organism()
  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val worldView = WorldView(organisms = Set(organism1), cells = Set(cell1))

  startWith(Idle, worldView)
  when(Idle) {
    case Event(Tick, worldView) =>
      socket ! ZMQMessage(Seq(Frame(Nil), Frame(worldView.serialize)))
      stay
  }
  whenUnhandled {
    case Event(message: ZMQMessage, _) =>
      log.debug("Received: " + message)
      val s = new String(message.frames(1).payload.toArray, "UTF-8")
      val intention = Intention.deserialize(s)
      log.info("Intention: " + intention)
      stay
    case Event(message, _) ⇒
      log.warning("Received unknown event: " + message)
      goto(Error)
  }
  initialize
}
