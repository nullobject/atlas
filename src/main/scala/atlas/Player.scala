package atlas

import akka.actor.{Actor, ActorRef, FSM, Status}
import akka.agent.Agent
import java.util.UUID
import scala.concurrent.stm._
import spray.json._

/**
 * The player FSM receives action events and executes them on the world agent.
 */
class Player(playerId: UUID, worldAgent: Agent[World]) extends Actor with FSM[Player.StateName, Player.StateData] {
  import Player._

  startWith(Uninitialised, StateData(List.empty))

  when(Uninitialised) {
    case Event(List(Action.Idle), stateData) =>
      goto(Idle) using spawn(stateData)

    case Event(_, _) =>
      sender ! Status.Failure(UninitializedException)
      stay
  }

  when(Idle) {
    case Event(actions: List[Action], stateData) =>
      atomic { txn =>
        val world = worldAgent.get
        actions.map { action =>
          action match {
            case Action.Idle =>
              // Do nothing.
            case Action.Move(organismId, direction) =>
              val organism = world.getOrgansim(organismId)
              organism map { o => worldAgent.send(_.move(o, direction)) }
            case Action.Eat(organismId) =>
              val organism = world.getOrgansim(organismId)
              organism map { o => worldAgent.send(_.eat(o)) }
            case Action.Drink(organismId) =>
              val organism = world.getOrgansim(organismId)
              organism map { o => worldAgent.send(_.drink(o)) }
          }
        }
      }
      stay using stateData.addSender(sender)
  }

  whenUnhandled {
    case Event(Game.Tick, stateData) =>
      val world = worldAgent.get
      val worldView = WorldView.scopeToPlayer(playerId, world)
      stateData.senders.map { _ ! worldView }
      stay using stateData.copy(senders = List.empty)
  }

  initialize

  def spawn(stateData: StateData): StateData = {
    val genome = Genome("Rat", Map("ReproduceFrequency" -> 200, "EatFrequency" -> 100, "DrinkFrequency" -> 50))
    val organism = Organism(playerId = playerId, genome = genome)
    worldAgent.send(_.spawn(organism))
    stateData.addSender(sender)
  }
}

object Player {
  abstract class InvalidActionException(message: String) extends RuntimeException(message)

  case object InvalidOrganismException extends InvalidActionException("Organsim does not belong to player")
  case object UninitializedException   extends InvalidActionException("Player not initialized")
  case object UnknownOrganismException extends InvalidActionException("Organsim does not exist")

  sealed trait Action

  object Action {
    import JsonFormats._

    // Do nothing.
    case object Idle extends Action

    // The given organism should consume a unit of food in the current cell.
    case class Eat(organismId: UUID) extends Action

    // The given organism should consume a unit of water in the current cell.
    case class Drink(organismId: UUID) extends Action

    // The given organism should move in the given direction.
    case class Move(organismId: UUID, direction: Vector2) extends Action

    def deserialize(value: String) = value.asJson.convertTo[Action]
  }

  case class Request(playerId: UUID, actions: List[Action])

  sealed trait StateName
  case object Uninitialised extends StateName
  case object Idle extends StateName

  case class StateData(senders: List[ActorRef]) {
    def addSender(sender: ActorRef) = copy(senders = senders :+ sender)
  }
}
