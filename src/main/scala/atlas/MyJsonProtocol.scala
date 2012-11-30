package atlas

import spray.json._

object MyJsonProtocol extends DefaultJsonProtocol {
  /* implicit object GeneFormat extends JsonFormat[Gene] { */
  /*   def write(x: Gene) = JsObject( */
  /*     "name"  -> JsString(x.getClass.getSimpleName), */
  /*     "value" -> JsNumber(x.value) */
  /*   ) */

  /*   def read(value: JsValue) = value.asJsObject.getFields("name", "value") match { */
  /*     case List(JsString(name), JsNumber(value)) => Gene.build(name, value.doubleValue) */
  /*     case _ => throw new DeserializationException("Gene expected") */
  /*   } */
  /* } */

  implicit val genomeFormat = jsonFormat2(Genome.apply)
}
