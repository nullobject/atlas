package atlas

import akka.actor.{Actor, ActorRef, FSM}
import java.util.UUID
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object Game {
  sealed trait State
  case object Idle extends State

  case class StateData(world: World, callbacks: Map[ActorRef, Any])

  sealed trait Message
  case object Tick extends Message
}

/**
 * The game FSM receives players' intentions from the server and executes them.
 * On every tick event the game responds to the players' requests with their
 * world views.
 *
 * TODO: send the latest world state when responding to clients.
 */
class Game(world: World) extends Actor with FSM[Game.State, Game.StateData] {
  import context.dispatcher
  import Game._

  override def preStart {
    context.system.scheduler.schedule(0.1 second, 0.1 second, self, Tick)
  }

  override def postRestart(reason: Throwable) {
    // Don't call preStart, only schedule once.
  }

  startWith(Idle, StateData(world, Map.empty))

  when(Idle) {
    case Event(Tick, stateData) =>
      stateData.callbacks.foreach { case (actor, worldView) => actor ! worldView }
      stay using stateData.copy(world = stateData.world.tick.get, callbacks = Map.empty)

    case Event(Player.Intention(playerId, Player.Action.Idle), stateData) =>
      stay using processResult(stateData, playerId, Success(stateData.world))

    case Event(Player.Intention(playerId, Player.Action.Spawn), stateData) =>
      // TODO: choose a genome.
      val genome = Genome("Rat", Map("FeedAmount" -> 1, "FeedFrequency" -> 2, "ReproduceFrequency" -> 3))
      val organism = Organism(playerId = playerId, genome = genome)
      val result = stateData.world.spawn(organism)
      stay using processResult(stateData, playerId, result)

    case Event(Player.Intention(playerId, Player.Action.Move(id, direction)), stateData) =>
      val organism = stateData.world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = stateData.world.move(organism, direction)
      stay using processResult(stateData, playerId, result)

    case Event(Player.Intention(playerId, Player.Action.Eat(id)), stateData) =>
      val organism = stateData.world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = stateData.world.eat(organism)
      stay using processResult(stateData, playerId, result)

    case Event(Player.Intention(playerId, Player.Action.Drink(id)), stateData) =>
      val organism = stateData.world.getOrgansim(id).get
      if (organism.playerId != playerId)
        throw new RuntimeException("Not your organsim")
      val result = stateData.world.drink(organism)
      stay using processResult(stateData, playerId, result)
  }

  initialize

  private def processResult(stateData: StateData, playerId: UUID, result: Try[World]) = result match {
    case Success(world) =>
      val callbacks = stateData.callbacks + (sender -> WorldView.scopeToPlayer(playerId, world))
      stateData.copy(world = world, callbacks = callbacks)
    case Failure(e) =>
      val callbacks = stateData.callbacks + (sender -> akka.actor.Status.Failure(e))
      stateData.copy(callbacks = callbacks)
  }
}
