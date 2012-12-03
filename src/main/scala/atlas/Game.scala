package atlas

import akka.actor.{Actor, FSM}
import java.util.UUID
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

    // Consume a unit of food in the current cell.
    case class Eat(organismId: UUID) extends Intention

    // Consume a unit of water in the current cell.
    case class Drink(organismId: UUID) extends Intention

    // Move the organism in the given direction.
    case class Move(organismId: UUID, direction: Vector2) extends Intention

    def deserialize(value: String) = value.asJson.convertTo[Intention]
  }
}

// The game FSM.
class Game extends Actor with FSM[Game.State, World] {
  import Game._

  startWith(Idle, World(cells = Set.empty))
  when(Idle) {
    case Event(Tick, world) =>
      stay using world.tick
    case Event(Intention.Idle, world) =>
      sender ! world
      stay
    case Event(Intention.Move(id, direction), world) =>
      /* val organism = world.getOrganismById(id).get */
      /* val newWorld = world.move(organism, direction) */
      val newWorld = world
      sender ! newWorld
      stay using newWorld
    case Event(Intention.Eat(id), world) =>
      val organism = world.getOrganismById(id).get
      stay using world.eat(organism)
    case Event(Intention.Drink(id), world) =>
      val organism = world.getOrganismById(id).get
      stay using world.drink(organism)
  }
  initialize
}
