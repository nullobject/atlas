package atlas

import akka.actor.{Actor, FSM}

object Game {
  sealed trait State
  case object Idle extends State

  sealed trait Message

  // Move the player in the given direction.
  case class Move(player: Player, direction: Vector2) extends Message

  // Consume a unit of food in the current cell.
  case class Eat(player: Player) extends Message

  // Consume a unit of water in the current cell.
  case class Drink(player: Player) extends Message
}

// The game finite state machine.
class Game extends Actor with FSM[Game.State, World] {
  import Game._

  startWith(Idle, World(cells = Set.empty))
  when(Idle) {
    case Event(Move(player, direction), world) =>
      stay using world.move(player, direction)
    case Event(Eat(player), world) =>
      stay using world.eat(player)
    case Event(Drink(player), world) =>
      stay using world.drink(player)
  }
  initialize
}
