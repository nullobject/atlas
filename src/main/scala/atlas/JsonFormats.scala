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

  implicit object IntentionFormat extends RootJsonFormat[Intention] {
    def write(intention: Intention) = intention match {
      case Intention.Idle | Intention.Eat | Intention.Drink => JsObject(
        "action" -> JsString(intention.getClass.getSimpleName)
      )
      case Intention.Move(organismId, direction) => JsObject(
        "action" -> JsString(intention.getClass.getSimpleName),
        "organismId" -> organismId.toJson,
        "direction" -> direction.toJson
      )
      case _ => throw new DeserializationException("Unknown intention")
    }

    def read(value: JsValue) = {
      val fields = value.asJsObject.fields
      fields.get("action").get match {
        case JsString("Idle")  => Intention.Idle
        case JsString("Eat")   => Intention.Eat
        case JsString("Drink") => Intention.Drink
        case JsString("Move")  =>
          Intention.Move(fields.get("organismId").get.convertTo[UUID], fields.get("direction").get.convertTo[Vector2])
        case _ => throw new DeserializationException("Intention expected")
      }
    }
  }

  implicit val genomeFormat    = jsonFormat2(Genome.apply)
  implicit val organismFormat  = jsonFormat3(Organism.apply)
  implicit val cellFormat      = jsonFormat4(Cell.apply)
  implicit val worldViewFormat = jsonFormat2(WorldView.apply)
}
