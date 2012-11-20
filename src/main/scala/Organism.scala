import akka.actor.{Actor, ActorRef, FSM}

object Organism {
  sealed trait State
  case object Idle    extends State
  case object Feeding extends State
  case object Dead    extends State

  case class Data(genome: Genome, health: Int)
}

/*
 * An organism represents a genome expressed in a living entity.
 */
class Organism extends Actor with FSM[Organism.State, Organism.Data] {
  import Organism._
}
