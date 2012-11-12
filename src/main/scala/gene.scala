abstract class Gene(val value: BigDecimal)

case class FeedFrequency(override val value: BigDecimal) extends Gene(value)
case class FeedAmount(override val value: BigDecimal) extends Gene(value)
case class ReproduceFrequency(override val value: BigDecimal) extends Gene(value)
case class Speed(override val value: BigDecimal) extends Gene(value)

object Gene {
  def build(name: String, value: BigDecimal): Gene = name match {
    case "FeedFrequency"      => FeedFrequency(value)
    case "FeedAmount"         => FeedAmount(value)
    case "ReproduceFrequency" => ReproduceFrequency(value)
    case "Speed"              => Speed(value)
    case _                    => throw new Error("Unknown gene: " + name)
  }

  def mix(genes: Seq[Gene]): Gene = {
    val value: BigDecimal = (BigDecimal(0) /: genes) { _ + _.value } / genes.size
    genes.head.getClass.getConstructor(classOf[BigDecimal]).newInstance(value)
    /* genes.head.build(value) */
  }
}
