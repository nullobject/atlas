package atlas

import akka.actor.{Actor, FSM}
import java.util.UUID
import scala.util.{Try, Success, Failure}
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

    case Event(Intention.Idle, world) =>
      sender ! WorldView(world)
      stay

    case Event(Intention.Spawn, world) =>
      val organism = Organism()
      val result = world.spawn(organism)
      processResult(result)

    case Event(Intention.Move(id, direction), world) =>
      val organism = world.getOrganismById(id).get
      val result = world.move(organism, direction)
      processResult(result)

    case Event(Intention.Eat(id), world) =>
      val organism = world.getOrganismById(id).get
      val result = world.eat(organism)
      processResult(result)

    case Event(Intention.Drink(id), world) =>
      val organism = world.getOrganismById(id).get
      val result = world.drink(organism)
      processResult(result)
  }

  initialize

  private def processResult(result: Try[World]) = result match {
    case Success(world) =>
      sender ! WorldView(world)
      stay using world
    case Failure(e) =>
      sender ! akka.actor.Status.Failure(e)
      stay
  }
}
