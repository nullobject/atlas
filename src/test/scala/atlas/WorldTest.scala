package atlas

import org.scalatest.FunSpec

class WorldTest extends FunSpec {
  val organism1 = Organism()
  val organism2 = Organism()
  val organism3 = Organism()
  val organism4 = Organism()

  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val cell2 = Cell(position = (1, 0), organisms = Set(organism2, organism3))
  val cell3 = Cell(position = (0, 1))
  val cell4 = Cell(position = (1, 1))

  val world = World(cells = Set(cell1, cell2, cell3, cell4))

  describe("#getOrganismById") {
    it("should return the organism with the given ID") {
      assert(world.getOrganismById(organism1.id) === Some(organism1))
      assert(world.getOrganismById(organism2.id) === Some(organism2))
      assert(world.getOrganismById(organism3.id) === Some(organism3))
      assert(world.getOrganismById(organism4.id) === None)
    }
  }

  describe("#getCellAtPosition") {
    it("should return the cell at the given position") {
      assert(world.getCellAtPosition(0, 0) === Some(cell1))
      assert(world.getCellAtPosition(1, 0) === Some(cell2))
      assert(world.getCellAtPosition(0, 1) === Some(cell3))
      assert(world.getCellAtPosition(1, 1) === Some(cell4))
      assert(world.getCellAtPosition(2, 2) === None)
    }
  }

  describe("#getCellForOrganism") {
    it("should return the cell containing the given organism") {
      assert(world.getCellForOrganism(organism1) === Some(cell1))
      assert(world.getCellForOrganism(organism2) === Some(cell2))
      assert(world.getCellForOrganism(organism3) === Some(cell2))
      assert(world.getCellForOrganism(organism4) === None)
    }
  }

  describe("#getAdjacentCell") {
    it("should return the cell adjacent to the given cell in the given direction") {
      assert(world.getAdjacentCell(cell1, Direction.N)  === None)
      assert(world.getAdjacentCell(cell1, Direction.NE) === None)
      assert(world.getAdjacentCell(cell1, Direction.E)  === Some(cell2))
      assert(world.getAdjacentCell(cell1, Direction.SE) === Some(cell4))
      assert(world.getAdjacentCell(cell1, Direction.S)  === Some(cell3))
      assert(world.getAdjacentCell(cell1, Direction.SW) === None)
      assert(world.getAdjacentCell(cell1, Direction.W)  === None)
      assert(world.getAdjacentCell(cell1, Direction.NW) === None)
    }
  }

  describe("#move") {
    it("should move the given organism in the given direction") {
      assert(world.getCellForOrganism(organism1).get.position === (0, 0))
      val newWorld = world.move(organism1, Direction.S)
      assert(newWorld.getCellForOrganism(organism1).get.position === (0, 1))
    }
  }

  describe("#eat") {
    it("should decrement the food in the cell containing the given organism") {
      assert(world.getCellForOrganism(organism1).get.food === 100)
      val newWorld = world.eat(organism1)
      assert(newWorld.getCellForOrganism(organism1).get.food === 99)
    }
  }

  describe("#drink") {
    it("should decrement the water in the cell containing the given organism") {
      assert(world.getCellForOrganism(organism1).get.water === 100)
      val newWorld = world.drink(organism1)
      assert(newWorld.getCellForOrganism(organism1).get.water === 99)
    }
  }
}
