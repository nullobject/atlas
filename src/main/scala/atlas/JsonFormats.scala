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
      case _ => throw new DeserializationException("Color expected")
    }
  }

  implicit object IntentionFormat extends RootJsonFormat[Game.Intention] {
    def write(intention: Game.Intention) = intention match {
      case Game.Intention.Idle => JsObject(
        "action" -> JsString(intention.getClass.getSimpleName)
      )
      case Game.Intention.Eat(organismId) => JsObject(
        "action" -> JsString(intention.getClass.getSimpleName),
        "organismId" -> organismId.toJson
      )
      case Game.Intention.Drink(organismId) => JsObject(
        "action" -> JsString(intention.getClass.getSimpleName),
        "organismId" -> organismId.toJson
      )
      case Game.Intention.Move(organismId, direction) => JsObject(
        "action" -> JsString(intention.getClass.getSimpleName),
        "organismId" -> organismId.toJson,
        "direction" -> direction.toJson
      )
      case _ => throw new DeserializationException("Unknown intention")
    }

    def read(value: JsValue) = {
      val fields = value.asJsObject.fields
      fields.get("action").get match {
        case JsString("Idle") =>
          Game.Intention.Idle
        case JsString("Eat") =>
          Game.Intention.Eat(fields.get("organismId").get.convertTo[UUID])
        case JsString("Drink") =>
          Game.Intention.Drink(fields.get("organismId").get.convertTo[UUID])
        case JsString("Move")  =>
          Game.Intention.Move(fields.get("organismId").get.convertTo[UUID], fields.get("direction").get.convertTo[Vector2])
        case _ => throw new DeserializationException("Intention expected")
      }
    }
  }

  implicit val genomeFormat    = jsonFormat2(Genome.apply)
  implicit val organismFormat  = jsonFormat3(Organism.apply)
  implicit val cellFormat      = jsonFormat4(Cell.apply)
  implicit val worldViewFormat = jsonFormat2(WorldView.apply)
}
