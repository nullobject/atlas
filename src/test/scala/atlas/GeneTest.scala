package atlas

import org.scalatest.FunSpec

class GeneTest extends FunSpec {
  describe(".mix") {
    it("should mix the sequence of genes") {
      val genes = Seq(FeedFrequency(0.1), FeedFrequency(0.2), FeedFrequency(0.3), FeedFrequency(0.4))
      val result = Gene.mix(genes)
      assert(result.isInstanceOf[FeedFrequency])
      assert(result.value === 0.25)
    }
  }

  describe(".correlation") {
    it("should calculate the Pearson product-moment correlation coefficient") {
      val xs = Seq(1.0, 2.0, 3.0)
      val ys = Seq(1.0, 3.0, 2.0)
      assert(Gene.correlation(xs, ys) === 0.5)
    }

    it("should throw an error when the sequences have different sizes") {
      intercept[IllegalArgumentException] {
        val xs = Seq(1.0, 2.0, 3.0)
        val ys = Seq(1.0, 3.0)
        Gene.correlation(xs, ys)
      }
    }

    it("should throw an error when the sequences are empty") {
      intercept[IllegalArgumentException] {
        val xs = Seq.empty
        val ys = Seq.empty
        Gene.correlation(xs, ys)
      }
    }
  }
}
