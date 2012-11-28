package atlas

import akka.actor.{Actor, ActorRef, FSM}

object Player {
  sealed trait State
  case object Idle    extends State
  case object Feeding extends State
  case object Dead    extends State

  case class Data(genome: Genome, health: Int)
}

class Player extends Actor with FSM[Player.State, Player.Data] {
  import Player._
}
