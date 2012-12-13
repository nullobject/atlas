package atlas

import akka.actor.{Actor, ActorRef, FSM, Status}
import akka.agent.Agent
import java.util.UUID
import spray.json._

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

  case class Request(playerId: UUID, action: Action)

  sealed trait StateName
  case object Uninitialised extends StateName
  case object Idle extends StateName

  case class StateData(senders: List[ActorRef]) {
    def addSender(sender: ActorRef) = copy(senders = senders :+ sender)
  }
}

/**
 * The player FSM receives action events and executes them on the world agent.
 */
class Player(playerId: UUID, worldAgent: Agent[World]) extends Actor with FSM[Player.StateName, Player.StateData] {
  import Player._

  startWith(Uninitialised, StateData(List.empty))

  when(Uninitialised) {
    case Event(Action.Idle, stateData) =>
      // TODO: spawn an organism.
      val genome = Genome("Rat", Map("FeedAmount" -> 1, "FeedFrequency" -> 2, "ReproduceFrequency" -> 3))
      val organism = Organism(playerId = playerId, genome = genome)
      worldAgent.send(_.spawn(organism))
      goto(Idle) using stateData.addSender(sender)

    case Event(_: Action, _) =>
      sender ! Status.Failure(UninitializedException)
      stay
  }

  when(Idle) {
    case Event(Action.Idle, stateData) =>
      stay using stateData.addSender(sender)

    case Event(Action.Move(organismId, direction), stateData) =>
      val world = worldAgent.get
      val organism = world.getOrgansim(organismId)
      if (organism.isEmpty) {
        sender ! Status.Failure(UnknownOrganismException)
        stay
      } else if (organism.get.playerId != playerId) {
        sender ! Status.Failure(InvalidOrganismException)
        stay
      } else {
        worldAgent.send(_.move(organism.get, direction))
        stay using stateData.addSender(sender)
      }

    case Event(Action.Eat(organismId), stateData) =>
      val world = worldAgent.get
      val organism = world.getOrgansim(organismId)
      if (organism.isEmpty) {
        sender ! Status.Failure(UnknownOrganismException)
        stay
      } else if (organism.get.playerId != playerId) {
        sender ! Status.Failure(InvalidOrganismException)
        stay
      } else {
        worldAgent.send(_.eat(organism.get))
        stay using stateData.addSender(sender)
      }

    case Event(Action.Drink(organismId), stateData) =>
      val world = worldAgent.get
      val organism = world.getOrgansim(organismId)
      if (organism.isEmpty) {
        sender ! Status.Failure(UnknownOrganismException)
        stay
      } else if (organism.get.playerId != playerId) {
        sender ! Status.Failure(InvalidOrganismException)
        stay
      } else {
        worldAgent.send(_.drink(organism.get))
        stay using stateData.addSender(sender)
      }
  }

  whenUnhandled {
    case Event(Game.Tick, stateData) =>
      val world = worldAgent.get
      stateData.senders.map { _ ! WorldView.scopeToPlayer(playerId, world) }
      stay using stateData.copy(senders = List.empty)
  }

  initialize
}