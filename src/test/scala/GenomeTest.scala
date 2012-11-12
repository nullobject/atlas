import org.scalatest.FunSpec

class GenomeTest extends FunSpec {
  val rabbit = Genome("Rabbit", Set(FeedAmount(1), FeedFrequency(2), ReproduceFrequency(3)))
  val pig = Genome("Pig", Set(FeedFrequency(4), ReproduceFrequency(5), Speed(6)))

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
      val rabbit = Genome.deserialize(json)
      assert(rabbit.name === "rabbit")
      assert(rabbit.genes === Set(FeedFrequency(1.23)))
    }
  }
  describe("#serialize") {
    it("should convert the genome into a JSON string") {
      assert(rabbit.serialize === """{"name":"Rabbit","genes":[{"name":"FeedAmount","value":1},{"name":"FeedFrequency","value":2},{"name":"ReproduceFrequency","value":3}]}""")
    }
  }
  describe("#*") {
    it("should mix each gene with the given genome") {
      assert(rabbit * pig === Genome("Rabbit", Set(FeedAmount(1), FeedFrequency(3), ReproduceFrequency(4), Speed(6))))
    }
  }
}
