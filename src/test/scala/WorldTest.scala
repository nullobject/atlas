import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.FunSpec

class WorldDataTest extends FunSpec {
  implicit lazy val system = ActorSystem()

  val organism1 = TestActorRef[Organism]
  val organism2 = TestActorRef[Organism]
  val organism3 = TestActorRef[Organism]
  val organism4 = TestActorRef[Organism]

  val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
  val cell2 = Cell(position = (1, 0), organisms = Set(organism2, organism3))
  val cell3 = Cell(position = (0, 1), organisms = Set.empty)
  val cell4 = Cell(position = (1, 1), organisms = Set.empty)

  val data = World.Data(cells = Set(cell1, cell2, cell3, cell4))

  describe(".getCellForOrgansim") {
    it("should return the cell containing the given organism") {
      assert(data.getCellForOrgansim(organism1) === Some(cell1))
      assert(data.getCellForOrgansim(organism2) === Some(cell2))
      assert(data.getCellForOrgansim(organism3) === Some(cell2))
      assert(data.getCellForOrgansim(organism4) === None)
    }
  }

  describe(".getAdjacentCell") {
    it("should return the cell adjacent to the given cell in the given direction") {
      assert(data.getAdjacentCell(cell1, World.S) === Some(cell3))
    }
  }

  describe(".moveOrganism") {
    it("should move the given organism in the given direction") {
      assert(data.getCellForOrgansim(organism1).get.position === (0, 0))
      val newData = World.moveOrganism(data, organism1, World.S)
      assert(newData.getCellForOrgansim(organism1).get.position === (0, 1))
    }
  }

  /* system.shutdown() */
}
