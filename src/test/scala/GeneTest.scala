import org.scalatest.FunSpec

class GeneTest extends FunSpec {
  describe(".mix") {
    it("should mix the sequence of genes") {
      val genes = Seq(FeedFrequency(0.1), FeedFrequency(0.2), FeedFrequency(0.3))
      val result = Gene.mix(genes)
      assert(result.isInstanceOf[FeedFrequency])
      assert(result.value === 0.2)
    }
  }
}
