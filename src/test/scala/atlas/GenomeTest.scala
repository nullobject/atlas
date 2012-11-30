package atlas

import org.scalatest.FunSpec

class GenomeTest extends FunSpec {
  val rabbit = Genome("Rabbit", Map("FeedAmount" -> 1, "FeedFrequency" -> 2, "ReproduceFrequency" -> 3))
  val pig = Genome("Pig", Map("FeedFrequency" -> 4, "ReproduceFrequency" -> 5, "Speed" -> 6))

  describe(".deserialize") {
    it("should parse the given JSON string") {
      val json = """{"name":"Fox","genes":{"FeedFrequency":1.23}}"""
      val fox = Genome.deserialize(json)
      assert(fox === Genome("Fox", Map("FeedFrequency" -> 1.23)))
    }
  }

  describe("#serialize") {
    it("should convert the genome into a JSON string") {
      assert(rabbit.serialize === """{"name":"Rabbit","genes":{"FeedAmount":1.0,"FeedFrequency":2.0,"ReproduceFrequency":3.0}}""")
    }
  }

  describe("#*") {
    it("should mix each gene with the given genome") {
      assert(rabbit * pig === Genome("Rabbit", Map("FeedAmount" -> 1, "FeedFrequency" -> 3, "ReproduceFrequency" -> 4, "Speed" -> 6)))
    }
  }

  describe("#similarity") {
    it("should return the similarity of the two genomes") {
      assert(rabbit.similarity(pig) === 1.0)
    }
  }
}
