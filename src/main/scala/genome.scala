import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.lang.reflect.{Type, ParameterizedType}

case class Genome(name: String, genes: Seq[Gene]) {
  // Crosses this genome with the given genome to produce a new genome.
  def *(other: Genome): Genome = {
    val genes = (this.genes ++ other.genes)
      .groupBy(_.getClass)
      .map { case (_, genes) => Gene.mix(genes) }
      .toSeq
    Genome(name, genes)
  }
}

object Genome {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def deserialize(value: String): Genome =
    mapper.readValue(value, new TypeReference[Genome]() {
      override def getType = new ParameterizedType {
        val getActualTypeArguments = manifest[Genome].typeArguments.map(_.erasure.asInstanceOf[Type]).toArray
        val getRawType = manifest[Genome].erasure
        val getOwnerType = null
      }
    })
}
