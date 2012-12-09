package atlas

import akka.actor.{Actor, FSM}
import java.util.UUID
import scala.util.{Failure, Success, Try}
import spray.json._
import JsonFormats._

object Game {
  sealed trait State
  case object Idle extends State

  sealed trait Message
  case object Tick extends Message

  sealed trait Intention

  object Intention {
    // Do nothing.
    case object Idle extends Intention

    // Spawns an organsim.
    case object Spawn extends Intention

    // The given organism should consume a unit of food in the current cell.
    case class Eat(organismId: UUID) extends Intention

    // The given organism should consume a unit of water in the current cell.
    case class Drink(organismId: UUID) extends Intention

    // The given organism should move in the given direction.
    case class Move(organismId: UUID, direction: Vector2) extends Intention

    def deserialize(value: String) = value.asJson.convertTo[Intention]
  }

  case class PlayerIntention(playerId: UUID, intention: Intention)
}

/**
 * The game FSM.
 */
class Game(world: World) extends Actor with FSM[Game.State, World] {
  import Game._

  startWith(Idle, world)

  when(Idle) {
    case Event(Tick, world) =>
      stay using world.tick.get

    case Event(PlayerIntention(playerId, Intention.Idle), world) =>
      sender ! WorldView(world)
      stay

    case Event(PlayerIntention(playerId, Intention.Spawn), world) =>
      val genome = Genome("Rat", Map("FeedAmount" -> 1, "FeedFrequency" -> 2, "ReproduceFrequency" -> 3))
      val organism = Organism(playerId = playerId, genome = genome)
      val result = world.spawn(organism)
      stay using processResult(result)

    case Event(PlayerIntention(playerId, Intention.Move(id, direction)), world) =>
      val organism = world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = world.move(organism, direction)
      stay using processResult(result)

    case Event(PlayerIntention(playerId, Intention.Eat(id)), world) =>
      val organism = world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = world.eat(organism)
      stay using processResult(result)

    case Event(PlayerIntention(playerId, Intention.Drink(id)), world) =>
      val organism = world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = world.drink(organism)
      stay using processResult(result)
  }

  initialize

  private def processResult(result: Try[World]) = result match {
    case Success(world) =>
      // TODO: scope world view to player.
      sender ! WorldView(world)
      world
    case Failure(e) =>
      sender ! akka.actor.Status.Failure(e)
      stateData
  }
}
