/* import com.fasterxml.jackson.annotation.JsonProperty; */
import com.fasterxml.jackson.core.`type`.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.lang.reflect.{Type, ParameterizedType}

case class Gene(name: String, value: Long)

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

  val json = """{"name":"FeedFrequency","value":1}"""
  val node = deserialize[Gene](json)

  def main(args: Array[String]) = println(node)
}
