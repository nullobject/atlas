package atlas

import scala.math.sqrt

object Math {
  /**
   * Computes the Pearson correlation coefficient between the two vectors.
   * Code adapted from Wikipedia:
   *   http://en.wikipedia.org/wiki/Pearson%27s_correlation_coefficient
   */
  def correlation(xs: List[Double], ys: List[Double]): Double = {
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
