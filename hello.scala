import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.`type`.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.lang.reflect.{Type, ParameterizedType}

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.PROPERTY, property="name")
abstract class Gene(value: Long)
case class FeedFrequency(value: Long) extends Gene(value)
case class ReproduceFrequency(value: Long, minParents: Long) extends Gene(value)

object Hello {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def deserialize[T: Manifest](value: String): T =
    mapper.readValue(value, new TypeReference[T]() {
      override def getType = new ParameterizedType {
        val getActualTypeArguments = manifest[T].typeArguments.map(_.erasure.asInstanceOf[Type]).toArray
        val getRawType = manifest[T].erasure
        val getOwnerType = null
      }
    })

  val json = """[{"name":"FeedFrequency","value":1},{"name":"ReproduceFrequency","value":2,"minParents":2}]"""
  val result = deserialize[Seq[Gene]](json)

  def main(args: Array[String]) = println(result)
}
