package atlas

import spray.json._
import JsonFormats._

sealed trait Intention

object Intention {
  case object Idle extends Intention
  case object Eat extends Intention
  case object Drink extends Intention
  case class Move(organismId: String, direction: Vector2) extends Intention

  def deserialize(value: String) = value.asJson.convertTo[Intention]
}
