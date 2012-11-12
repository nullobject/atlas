import spray.json._

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit object GeneFormat extends JsonFormat[Gene] {
    def write(x: Gene) = JsObject(
      "name"  -> JsString(x.getClass.getName),
      "value" -> JsNumber(x.value)
    )

    def read(value: JsValue) = value.asJsObject.getFields("name", "value") match {
      case Seq(JsString(name), JsNumber(value)) =>
        Gene.build(name, value)
      case _ =>
        throw new DeserializationException("Gene expected")
    }
  }

  implicit val genomeFormat = jsonFormat2(Genome.apply)
}
