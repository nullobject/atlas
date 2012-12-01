package atlas

import akka.actor.{Actor, FSM}

object Game {
  sealed trait State
  case object Idle extends State

  sealed trait Message

  // Move the organism in the given direction.
  case class Move(organism: Organism, direction: Vector2) extends Message

  // Consume a unit of food in the current cell.
  case class Eat(organism: Organism) extends Message

  // Consume a unit of water in the current cell.
  case class Drink(organism: Organism) extends Message
}

// The game finite state machine.
class Game extends Actor with FSM[Game.State, World] {
  import Game._

  startWith(Idle, World(cells = Set.empty))
  when(Idle) {
    case Event(Move(organism, direction), world) =>
      stay using world.move(organism, direction)
    case Event(Eat(organism), world) =>
      stay using world.eat(organism)
    case Event(Drink(organism), world) =>
      stay using world.drink(organism)
  }
  initialize
}
