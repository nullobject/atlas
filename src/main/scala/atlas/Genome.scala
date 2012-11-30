package atlas

import spray.json._
import FractionalImplicits._
import MyJsonProtocol._

// A genome is a set of genes.
case class Genome(name: String, genes: Map[String, Double]) {
  def serialize = this.toJson.compactPrint

  // Crosses this genome with the given genome to produce a new genome.
  def *(that: Genome) = {
    val newGenes = (this.genes.toList ++ that.genes.toList)
      .groupBy { case (name, genes) => name }
      .map { case (name, genes) => (name, genes.map { case (name, value) => value }.avg) }
    copy(genes = newGenes)
  }

  /**
   * Returns the similarity of this genome with the given genome by calculating
   * the correlation of the common genes.
   */
  def similarity(that: Genome) = {
    val keys = this.genes.keySet intersect that.genes.keySet
    val xs = this.genes.filterKeys(keys).values
    val ys = that.genes.filterKeys(keys).values
    Math.correlation(xs.toList, ys.toList)
  }
}

object Genome {
  def deserialize(value: String) = value.asJson.convertTo[Genome]
  def empty = Genome(name = "", genes = Map.empty)
}
