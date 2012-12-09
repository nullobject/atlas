package atlas

import java.util.UUID
import spray.json._
import JsonFormats._

object Player {
  sealed trait Action

  object Action {
    // Do nothing.
    case object Idle extends Action

    // Spawns an organsim.
    case object Spawn extends Action

    // The given organism should consume a unit of food in the current cell.
    case class Eat(organismId: UUID) extends Action

    // The given organism should consume a unit of water in the current cell.
    case class Drink(organismId: UUID) extends Action

    // The given organism should move in the given direction.
    case class Move(organismId: UUID, direction: Vector2) extends Action

    def deserialize(value: String) = value.asJson.convertTo[Action]
  }

  case class Intention(playerId: UUID, action: Action)
}
