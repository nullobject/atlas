package atlas

import spray.json._
import MyJsonProtocol._

// A genome is a set of genes.
case class Genome(name: String, genes: Set[Gene]) {
  def serialize = this.toJson.compactPrint

  // Crosses this genome with the given genome to produce a new genome.
  def *(that: Genome) = {
    val newGenes = (this.genes ++ that.genes)
      .groupBy(_.getClass)
      .map { case (_, genes) => Gene.mix(genes.toSeq) }
      .toSet
    copy(genes = newGenes)
  }
}

object Genome {
  def deserialize(value: String) = value.asJson.convertTo[Genome]
  def empty = Genome(name = "", genes = Set.empty)
}
