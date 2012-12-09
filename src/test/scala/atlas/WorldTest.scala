package atlas

import org.scalatest.FunSpec
import scala.util.{Failure, Success}

class WorldTest extends FunSpec {
  val organism1 = Organism()
  val organism2 = Organism()
  val organism3 = Organism()
  val organism4 = Organism()

  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val cell2 = Cell(position = (1, 0), organisms = Set(organism2, organism3), food = 0, water = 0)
  val cell3 = Cell(position = (0, 1))
  val cell4 = Cell(position = (1, 1))

  val world = World(cells = Set(cell1, cell2, cell3, cell4))

  describe("#organisms") {
    it("should return the organisms") {
      assert(world.organisms === Set(organism1, organism2, organism3))
    }
  }

  describe("#getOrgansim") {
    it("should return the organism with the given ID") {
      assert(world.getOrgansim(organism1.id) === Some(organism1))
      assert(world.getOrgansim(organism2.id) === Some(organism2))
      assert(world.getOrgansim(organism3.id) === Some(organism3))
      assert(world.getOrgansim(organism4.id) === None)
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

  describe("#getSurroundingCells") {
    it("should return the cells surrounding the given cell") {
      assert(world.getSurroundingCells(cell1) === Set(cell2, cell3, cell4))
    }
  }

  describe("#getCellsForPlayer") {
    it("should return the cells containing organisms for the given player") {
      assert(world.getCellsForPlayer(organism1.playerId) === Set(cell1))
    }
  }

  describe("#tick") {
    it("should increment the age") {
      assert(world.age === 0)
      val result = world.tick
      assert(result.get.age === 1)
    }
  }

  describe("#spawn") {
    it("should spawn the given organism") {
      assert(world.organisms.contains(organism4) === false)
      val result = world.spawn(organism4).get
      assert(result.organisms.contains(organism4) === true)
    }

    it("should fail when spawning an organism which has already spawned") {
      assert(world.organisms.contains(organism1) === true)
      val result = world.spawn(organism1)
      assert(result === Failure(World.InvalidOperationException("Organism already spawned")))
    }
  }

  describe("#move") {
    it("should move the given organism in the given direction") {
      assert(world.getCellForOrganism(organism1).get.position === (0, 0))
      val result = world.move(organism1, Direction.S).get
      assert(result.getCellForOrganism(organism1).get.position === (0, 1))
    }

    it("should fail when moving to an unkown cell") {
      assert(world.getCellForOrganism(organism1).get.position === (0, 0))
      val result = world.move(organism1, Direction.N)
      assert(result === Failure(World.InvalidOperationException("Invalid direction")))
    }
  }

  describe("#eat") {
    it("should decrement the food in the cell containing the given organism") {
      assert(world.getCellForOrganism(organism1).get.food === 100)
      val result = world.eat(organism1).get
      assert(result.getCellForOrganism(organism1).get.food === 99)
    }

    it("should fail when eating in a cell with no food") {
      assert(world.getCellForOrganism(organism2).get.food === 0)
      val result = world.eat(organism2)
      assert(result === Failure(World.InvalidOperationException("No food in cell")))
    }
  }

  describe("#drink") {
    it("should decrement the water in the cell containing the given organism") {
      assert(world.getCellForOrganism(organism1).get.water === 100)
      val result = world.drink(organism1).get
      assert(result.getCellForOrganism(organism1).get.water === 99)
    }

    it("should fail when drinking in a cell with no water") {
      assert(world.getCellForOrganism(organism2).get.water === 0)
      val result = world.drink(organism2)
      assert(result === Failure(World.InvalidOperationException("No water in cell")))
    }
  }
}
