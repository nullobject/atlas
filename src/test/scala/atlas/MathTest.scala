package atlas

import org.scalatest.FunSpec

class MathTest extends FunSpec {
  describe(".correlation") {
    it("should calculate the Pearson product-moment correlation coefficient") {
      val xs = List(1.0, 2.0, 3.0)
      val ys = List(1.0, 3.0, 2.0)
      assert(Math.correlation(xs, ys) === 0.5)
    }

    it("should throw an error when the sequences have different sizes") {
      intercept[IllegalArgumentException] {
        val xs = List(1.0, 2.0, 3.0)
        val ys = List(1.0, 3.0)
        Math.correlation(xs, ys)
      }
    }

    it("should throw an error when the sequences are empty") {
      intercept[IllegalArgumentException] {
        val xs = List.empty
        val ys = List.empty
        Math.correlation(xs, ys)
      }
    }
  }
}
