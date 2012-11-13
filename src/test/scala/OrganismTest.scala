import org.scalatest.FunSpec

class OrganismTest extends FunSpec {
  val rabbit = Genome("Rabbit", Set(FeedAmount(1), FeedFrequency(2), ReproduceFrequency(3)))
  val organism = Organism(rabbit, 100)

  describe(".tick") {
    it("should decrement the health") {
      assert(organism.tick.health === 99)
    }
  }
}
