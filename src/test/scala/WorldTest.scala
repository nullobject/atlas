import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, FunSpec}

object WorldTest {
  class EchoActor extends Actor {
    def receive = {
      case x => sender ! x
    }
  }
}

class WorldTest extends TestKit(ActorSystem()) with FunSpec with BeforeAndAfterAll {
  import WorldTest._

  override def afterAll { system.shutdown() }

  val organism1 = TestActorRef[EchoActor]
  val organism2 = TestActorRef[EchoActor]
  val organism3 = TestActorRef[EchoActor]
  val organism4 = TestActorRef[EchoActor]

  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val cell2 = Cell(position = (1, 0), organisms = Set(organism2, organism3))
  val cell3 = Cell(position = (0, 1))
  val cell4 = Cell(position = (1, 1))

  val world = World(cells = Set(cell1, cell2, cell3, cell4))

  describe("#getCellAtPosition") {
    it("should return the cell at the given position") {
      assert(world.getCellAtPosition(0, 0) === Some(cell1))
      assert(world.getCellAtPosition(1, 0) === Some(cell2))
      assert(world.getCellAtPosition(0, 1) === Some(cell3))
      assert(world.getCellAtPosition(1, 1) === Some(cell4))
      assert(world.getCellAtPosition(2, 2) === None)
    }
  }

  describe("#getCellForOrgansim") {
    it("should return the cell containing the given organism") {
      assert(world.getCellForOrgansim(organism1) === Some(cell1))
      assert(world.getCellForOrgansim(organism2) === Some(cell2))
      assert(world.getCellForOrgansim(organism3) === Some(cell2))
      assert(world.getCellForOrgansim(organism4) === None)
    }
  }

  describe("#getAdjacentCell") {
    it("should return the cell adjacent to the given cell in the given direction") {
      assert(world.getAdjacentCell(cell1, Game.N)  === None)
      assert(world.getAdjacentCell(cell1, Game.NE) === None)
      assert(world.getAdjacentCell(cell1, Game.E)  === Some(cell2))
      assert(world.getAdjacentCell(cell1, Game.SE) === Some(cell4))
      assert(world.getAdjacentCell(cell1, Game.S)  === Some(cell3))
      assert(world.getAdjacentCell(cell1, Game.SW) === None)
      assert(world.getAdjacentCell(cell1, Game.W)  === None)
      assert(world.getAdjacentCell(cell1, Game.NW) === None)
    }
  }

  describe("#move") {
    it("should move the given organism in the given direction") {
      assert(world.getCellForOrgansim(organism1).get.position === (0, 0))
      val newWorld = world.move(organism1, Game.S)
      assert(newWorld.getCellForOrgansim(organism1).get.position === (0, 1))
    }
  }

  describe("#eat") {
    it("should decrement the food in the cell containing the given organism") {
      assert(world.getCellForOrgansim(organism1).get.food === 100)
      val newWorld = world.eat(organism1)
      assert(newWorld.getCellForOrgansim(organism1).get.food === 99)
    }
  }

  describe("#drink") {
    it("should decrement the water in the cell containing the given organism") {
      assert(world.getCellForOrgansim(organism1).get.water === 100)
      val newWorld = world.drink(organism1)
      assert(newWorld.getCellForOrgansim(organism1).get.water === 99)
    }
  }
}
