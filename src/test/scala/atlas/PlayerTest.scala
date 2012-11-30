package atlas

import org.scalatest.FunSpec

class PlayerTest extends FunSpec {
  describe("#isSimilar") {
    it("should be true if the two organisms are similar") {
      val genome1 = Genome(name = "genome1", genes = Map("a" -> 1, "b" -> 2, "c" -> 3))
      val genome2 = Genome(name = "genome2", genes = Map("a" -> 4, "b" -> 5, "c" -> 6))
      val genome3 = Genome(name = "genome2", genes = Map("a" -> 1, "b" -> 3, "c" -> 2))
      val player1 = Player(genome = genome1)
      val player2 = Player(genome = genome2)
      val player3 = Player(genome = genome3)
      assert(player1.isSimilar(player2) === true)
      assert(player1.isSimilar(player3) === false)
    }
  }
}
