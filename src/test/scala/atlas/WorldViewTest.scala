package atlas

import org.scalatest.FunSpec
import scala.util.{Failure, Success}

class WorldViewTest extends FunSpec {
  val organism1 = Organism()
  val organism2 = Organism()
  val organism3 = Organism()

  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val cell2 = Cell(position = (1, 0))
  val cell3 = Cell(position = (2, 0))
  val cell4 = Cell(position = (0, 1))
  val cell5 = Cell(position = (1, 1), organisms = Set(organism2))
  val cell6 = Cell(position = (2, 1))
  val cell7 = Cell(position = (0, 2))
  val cell8 = Cell(position = (1, 2))
  val cell9 = Cell(position = (2, 2), organisms = Set(organism3))

  val world = World(cells = Set(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9))

  describe(".scopeToPlayer") {
    it("should return a world view scoped to the player's cells") {
     val result1 = WorldView.scopeToPlayer(organism1.playerId, world)
     assert(result1.cells === Set(cell1, cell2, cell4, cell5))

     val result2 = WorldView.scopeToPlayer(organism2.playerId, world)
     assert(result2.cells === Set(cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9))

     val result3 = WorldView.scopeToPlayer(organism3.playerId, world)
     assert(result3.cells === Set(cell5, cell6, cell8, cell9))
    }

    it("should return a world view with an age") {
     val result = WorldView.scopeToPlayer(organism1.playerId, world)
     assert(result.age === world.age)
    }
  }
}
