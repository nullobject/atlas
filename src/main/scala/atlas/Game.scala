package atlas

import akka.actor.{Actor, FSM}
import scala.util.{Failure, Success, Try}

object Game {
  sealed trait State
  case object Idle extends State

  sealed trait Message
  case object Tick extends Message
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

    case Event(Player.Intention(playerId, Player.Action.Idle), world) =>
      sender ! WorldView(world)
      stay

    case Event(Player.Intention(playerId, Player.Action.Spawn), world) =>
      val genome = Genome("Rat", Map("FeedAmount" -> 1, "FeedFrequency" -> 2, "ReproduceFrequency" -> 3))
      val organism = Organism(playerId = playerId, genome = genome)
      val result = world.spawn(organism)
      stay using processResult(result)

    case Event(Player.Intention(playerId, Player.Action.Move(id, direction)), world) =>
      val organism = world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = world.move(organism, direction)
      stay using processResult(result)

    case Event(Player.Intention(playerId, Player.Action.Eat(id)), world) =>
      val organism = world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = world.eat(organism)
      stay using processResult(result)

    case Event(Player.Intention(playerId, Player.Action.Drink(id)), world) =>
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
