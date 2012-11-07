import org.scalatest.FunSpec

class GenomeTest extends FunSpec {
  describe("#*") {
    it("should mix each gene with the given genome") {
      pending
    }
  }
  describe(".deserialize") {
    it("should parse the given JSON string") {
      val json = """
        {
          "name": "rabbit",
          "genes": [
            {"name": "FeedFrequency", "value": 1.23}
          ]
        }
      """
      val genome = Genome.deserialize(json)
      assert(genome.name === "rabbit")
      assert(genome.genes === List(FeedFrequency(1.23)))
    }
  }
}
