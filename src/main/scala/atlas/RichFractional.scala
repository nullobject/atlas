package atlas

import scala.collection.GenTraversableOnce

class RichFractional[A](self: GenTraversableOnce[A]) {
  def avg(implicit n: Fractional[A]) = {
    val (total, count) = self.toIterator.foldLeft((n.zero, n.zero)) {
      case ((total, count), x) => (n.plus(total, x), n.plus(count, n.one))
    }
    n.div(total, count)
  }
}

object FractionalImplicits {
  implicit def fractional2RichFractional[A: Fractional](self: GenTraversableOnce[A]) = new RichFractional(self)
}
