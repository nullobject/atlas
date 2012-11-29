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

  /**
   * Computes the Pearson correlation coefficient between the two vectors.
   * Code adapted from Wikipedia:
   *   http://en.wikipedia.org/wiki/Pearson%27s_correlation_coefficient
   */
  def correlation(xs: Seq[Double], ys: Seq[Double]): Double = {
    if (xs.size != ys.size)
      throw new IllegalArgumentException("Sequences must be the same size")

    if (xs.isEmpty)
      throw new IllegalArgumentException("Sequences must not be empty")

    val n = xs.size
    var sumXSquared = 0.0
    var sumYSquared = 0.0
    var sumCoproduct = 0.0
    var meanX = xs(0)
    var meanY = ys(0)

    for (i <- 2 to n) {
      val sweep = (i - 1.0) / i
      val deltaX = xs(i - 1) - meanX
      val deltaY = ys(i - 1) - meanY
      sumXSquared += (deltaX * deltaX * sweep)
      sumYSquared += (deltaY * deltaY * sweep)
      sumCoproduct += (deltaX * deltaY * sweep)
      meanX += (deltaX / i)
      meanY += (deltaY / i)
    }

    val stdevX = sqrt(sumXSquared / n)
    val stdevY = sqrt(sumYSquared / n)
    val covar = sumCoproduct / n

    covar / (stdevX * stdevY)
  }
}
