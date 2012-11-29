package atlas

// A gene represents a parameter and a value.
abstract class Gene(val value: Double)

case class FeedAmount(override val value: Double) extends Gene(value)
case class FeedFrequency(override val value: Double) extends Gene(value)
case class ReproduceFrequency(override val value: Double) extends Gene(value)
case class Speed(override val value: Double) extends Gene(value)

object Gene {
  import scala.math.sqrt

  def build(name: String, value: Double): Gene = name match {
    case "FeedFrequency"      => FeedFrequency(value)
    case "FeedAmount"         => FeedAmount(value)
    case "ReproduceFrequency" => ReproduceFrequency(value)
    case "Speed"              => Speed(value)
    case _                    => throw new Error("Unknown gene: " + name)
  }

  def mix(genes: Seq[Gene]): Gene = {
    val value: Double = genes.foldLeft(0.0) { _ + _.value } / genes.size
    /* genes.head.getClass.getConstructor(classOf[Double]).newInstance(value) */
    Gene.build(genes.head.getClass.getSimpleName, value)
  }
}
