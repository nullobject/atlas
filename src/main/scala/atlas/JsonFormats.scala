package atlas

import java.util.UUID
import spray.json._

object JsonFormats extends DefaultJsonProtocol {
  implicit object UUIDFormat extends RootJsonFormat[UUID] {
    def write(id: UUID) = JsString(id.toString)
    def read(value: JsValue) = value match {
      case JsString(s) => UUID.fromString(s)
      case _ => throw new DeserializationException("UUID expected")
    }
  }

  implicit object Vector2Format extends RootJsonFormat[Vector2] {
    def write(v: Vector2) = JsArray(JsNumber(v.x), JsNumber(v.y))
    def read(value: JsValue) = value match {
      case JsArray(JsNumber(x) :: JsNumber(y) :: Nil) => Vector2(x.toInt, y.toInt)
      case _ => throw new DeserializationException("Vector2 expected")
    }
  }

  implicit object PlayerActionFormat extends RootJsonFormat[Player.Action] {
    def write(action: Player.Action) = action match {
      case Player.Action.Idle => JsObject(
        "action" -> JsString(action.getClass.getSimpleName)
      )
      case Player.Action.Eat(organismId) => JsObject(
        "action" -> JsString(action.getClass.getSimpleName),
        "organismId" -> organismId.toJson
      )
      case Player.Action.Drink(organismId) => JsObject(
        "action" -> JsString(action.getClass.getSimpleName),
        "organismId" -> organismId.toJson
      )
      case Player.Action.Move(organismId, direction) => JsObject(
        "action" -> JsString(action.getClass.getSimpleName),
        "organismId" -> organismId.toJson,
        "direction" -> direction.toJson
      )
      case _ => throw new DeserializationException("Unknown action")
    }

    def read(value: JsValue) = {
      val fields = value.asJsObject.fields
      fields.get("action").get match {
        case JsString("Idle") =>
          Player.Action.Idle
        case JsString("Eat") =>
          Player.Action.Eat(fields.get("organismId").get.convertTo[UUID])
        case JsString("Drink") =>
          Player.Action.Drink(fields.get("organismId").get.convertTo[UUID])
        case JsString("Move")  =>
          Player.Action.Move(fields.get("organismId").get.convertTo[UUID], fields.get("direction").get.convertTo[Vector2])
        case _ => throw new DeserializationException("Action expected")
      }
    }
  }

  implicit val playerRequestFormat = jsonFormat2(Player.Request.apply)
  implicit val genomeFormat        = jsonFormat2(Genome.apply)
  implicit val organismFormat      = jsonFormat7(Organism.apply)
  implicit val cellFormat          = jsonFormat4(Cell.apply)
  implicit val worldViewFormat     = jsonFormat2(WorldView.apply)
}
