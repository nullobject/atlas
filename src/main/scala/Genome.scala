import spray.json._
import MyJsonProtocol._

// A genome is a set of genes.
case class Genome(name: String, genes: Set[Gene]) {
  def serialize: String = {
    val ast = this.toJson
    ast.compactPrint
  }

  // Crosses this genome with the given genome to produce a new genome.
  def *(other: Genome): Genome = {
    val genes = (this.genes ++ other.genes)
      .groupBy(_.getClass)
      .map { case (_, genes) => Gene.mix(genes.toSeq) }
      .toSet
    Genome(name, genes)
  }
}

object Genome {
  def deserialize(value: String): Genome = {
    val ast = value.asJson
    ast.convertTo[Genome]
  }
}
