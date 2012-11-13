import akka.actor.{Actor, ActorRef, FSM}
import akka.util.duration._

object Organism {
  sealed trait State
  case object Idle    extends State
  case object Feeding extends State
  case object Dead    extends State

  case class Data(genome: Genome, health: BigDecimal)
}

// An organism represents a genome expressed in a living entity.
case class Organism(state: Organism.State, data: Organism.Data) {
  import Organism._

  def tick: Organism = copy(
    data = data.copy(health = data.health - 1)
  )
}
