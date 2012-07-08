import com.fasterxml.jackson.core.`type`.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.lang.reflect.{Type, ParameterizedType}

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

  val json = """{"name":"Rabbit","genes":[{"name":"FeedFrequency","value":1},{"name":"ReproduceFrequency","value":2,"minParents":2}]}"""
  val result = deserialize[Genome](json)

  def main(args: Array[String]) = println(result)
}
