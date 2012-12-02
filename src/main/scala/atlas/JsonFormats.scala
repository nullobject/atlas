package atlas

import java.util.UUID
import spray.json._

object JsonFormats extends DefaultJsonProtocol {
  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(id: UUID) = JsString(id.toString)
    def read(value: JsValue) = value match {
      case JsString(s) => UUID.fromString(s)
      case _ => throw new DeserializationException("UUID expected")
    }
  }

  implicit object Vector2Format extends JsonFormat[Vector2] {
    def write(v: Vector2) = JsArray(JsNumber(v.x), JsNumber(v.y))
    def read(value: JsValue) = value match {
      case JsArray(JsNumber(x) :: JsNumber(y) :: Nil) => Vector2(x.toInt, y.toInt)
      case _ => throw new DeserializationException("Color expected")
    }
  }

  implicit val genomeFormat = jsonFormat2(Genome.apply)
  implicit val organismFormat = jsonFormat3(Organism.apply)
  implicit val cellFormat = jsonFormat4(Cell.apply)
  implicit val worldViewFormat = jsonFormat2(WorldView.apply)
}
