import spray.json._
import MyJsonProtocol._

case class Genome(name: String, genes: Seq[Gene]) {
  // Crosses this genome with the given genome to produce a new genome.
  def *(other: Genome): Genome = {
    val genes = (this.genes ++ other.genes)
      .groupBy(_.getClass)
      .map { case (_, genes) => Gene.mix(genes) }
      .toSeq
    Genome(name, genes)
  }

  def serialize: String = {
    val ast = this.toJson
    ast.compactPrint
  }
}

object Genome {
  def deserialize(value: String): Genome = {
    val ast = value.asJson
    ast.convertTo[Genome]
  }
}
