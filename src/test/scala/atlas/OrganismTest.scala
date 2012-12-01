package atlas

import org.scalatest.FunSpec

class OrganismTest extends FunSpec {
  describe("#isSimilar") {
    it("should be true if the two organisms are similar") {
      val genome1 = Genome(name = "genome1", genes = Map("a" -> 1, "b" -> 2, "c" -> 3))
      val genome2 = Genome(name = "genome2", genes = Map("a" -> 4, "b" -> 5, "c" -> 6))
      val genome3 = Genome(name = "genome2", genes = Map("a" -> 1, "b" -> 3, "c" -> 2))
      val organism1 = Organism(genome = genome1)
      val organism2 = Organism(genome = genome2)
      val organism3 = Organism(genome = genome3)
      assert(organism1.isSimilar(organism2) === true)
      assert(organism1.isSimilar(organism3) === false)
    }
  }
}
