package atlas

import akka.actor.{Actor, ActorRef, FSM}

object Game {
  sealed trait State
  case object Idle extends State

  sealed trait Message

  // Move the organism in the given direction.
  case class Move(organism: ActorRef, direction: Vector2) extends Message

  // Consume a unit of food in the current cell.
  case class Eat(organism: ActorRef) extends Message

  // Consume a unit of water in the current cell.
  case class Drink(organism: ActorRef) extends Message
}

// The game finite state machine.
class Game extends Actor with FSM[Game.State, World] {
  import Game._

  startWith(Idle, World())
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
