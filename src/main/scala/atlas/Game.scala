package atlas

import akka.actor.{Actor, FSM}
import java.util.UUID

object Game {
  sealed trait State
  case object Idle extends State

  sealed trait Message

  case object Tick extends Message

  // Move the organism in the given direction.
  case class Move(id: UUID, direction: Vector2) extends Message

  // Consume a unit of food in the current cell.
  case class Eat(id: UUID) extends Message

  // Consume a unit of water in the current cell.
  case class Drink(id: UUID) extends Message
}

// The game FSM.
class Game extends Actor with FSM[Game.State, World] {
  import Game._

  startWith(Idle, World(cells = Set.empty))
  when(Idle) {
    case Event(Tick, world) => {
      stay using world.tick
      stay
    }
    case Event(Move(id, direction), world) => {
      val organism = world.getOrganismById(id).get
      stay using world.move(organism, direction)
    }
    case Event(Eat(id), world) => {
      val organism = world.getOrganismById(id).get
      stay using world.eat(organism)
    }
    case Event(Drink(id), world) => {
      val organism = world.getOrganismById(id).get
      stay using world.drink(organism)
    }
  }
  initialize
}
