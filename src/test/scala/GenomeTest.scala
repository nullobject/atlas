import org.scalatest.FunSpec

class GenomeTest extends FunSpec {
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
  describe("#serialize") {
    it("should convert the genome into a JSON string") {
      val genome = Genome("Rabbit", List(FeedFrequency(1.23)))
      assert(genome.serialize === """{"name":"Rabbit","genes":[{"name":"FeedFrequency","value":1.23}]}""")
    }
  }
  describe("#*") {
    it("should mix each gene with the given genome") {
      val rabbit = Genome("Rabbit", List(FeedAmount(1), FeedFrequency(2), ReproduceFrequency(3)))
      val pig    = Genome("Pig",    List(FeedFrequency(4), ReproduceFrequency(5), Speed(6)))
      assert(rabbit * pig === Genome("Rabbit", List(FeedAmount(1), Speed(6), ReproduceFrequency(4), FeedFrequency(3))))
    }
  }
}
