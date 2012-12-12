package atlas

import akka.actor.{Actor, FSM, Props}
import akka.agent.Agent
import java.util.UUID
import scala.concurrent.duration._
import scala.language.postfixOps

object Game {
  sealed trait StateName
  case object Idle extends StateName

  case class StateData(worldAgent: Agent[World])

  sealed trait Message
  case object Tick extends Message
}

/**
 * The game FSM receives players' intentions from the server and routes them
 * to a player actor. On every tick event the game forwards the players a tick
 * event.
 */
class Game(worldAgent: Agent[World]) extends Actor with FSM[Game.StateName, Game.StateData] {
  import context._
  import Game._

  override def preStart {
    system.scheduler.schedule(0.1 second, 0.1 second, self, Tick)
  }

  override def postRestart(reason: Throwable) {
    // Don't call preStart, only schedule once.
  }

  startWith(Idle, StateData(worldAgent))

  when(Idle) {
    case Event(Tick, _) =>
      children.foreach { _ ! Tick }
      stay

    case Event(Player.Intention(playerId, action), _) =>
      getPlayer(playerId).forward(action)
      stay
  }

  initialize

  def getPlayer(playerId: UUID) =
    child(s"player-$playerId").getOrElse(newPlayer(playerId))

  def newPlayer(playerId: UUID) =
    actorOf(Props(new Player(playerId, worldAgent)), name = s"player-$playerId")
}
