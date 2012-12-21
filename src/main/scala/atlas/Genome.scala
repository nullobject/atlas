package atlas

import scala.util.Random
import spray.json._
import FractionalImplicits._
import JsonFormats._

/**
 * A genome is a set of genes.
 */
case class Genome(name: String, genes: Map[String, Double]) {
  def apply(name: String) = genes(name)

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

  def mutate = {
    val newGenes = genes.map { case (name, value) => (name, value + (((Random.nextDouble * 2.0) - 1.0) * value * 0.05)) }
    copy(genes = newGenes)
  }
}

object Genome {
  def deserialize(value: String) = value.asJson.convertTo[Genome]
  def empty = Genome(name = "", genes = Map.empty)
}
