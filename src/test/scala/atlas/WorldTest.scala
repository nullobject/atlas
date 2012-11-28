package atlas

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, FunSpec}
import VectorImplicits._

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

  val player1 = TestActorRef[EchoActor]
  val player2 = TestActorRef[EchoActor]
  val player3 = TestActorRef[EchoActor]
  val player4 = TestActorRef[EchoActor]

  val cell1 = Cell(position = (0, 0), players = Set(player1))
  val cell2 = Cell(position = (1, 0), players = Set(player2, player3))
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

  describe("#getCellForPlayer") {
    it("should return the cell containing the given player") {
      assert(world.getCellForPlayer(player1) === Some(cell1))
      assert(world.getCellForPlayer(player2) === Some(cell2))
      assert(world.getCellForPlayer(player3) === Some(cell2))
      assert(world.getCellForPlayer(player4) === None)
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
    it("should move the given player in the given direction") {
      assert(world.getCellForPlayer(player1).get.position === (0, 0))
      val newWorld = world.move(player1, Direction.S)
      assert(newWorld.getCellForPlayer(player1).get.position === (0, 1))
    }
  }

  describe("#eat") {
    it("should decrement the food in the cell containing the given player") {
      assert(world.getCellForPlayer(player1).get.food === 100)
      val newWorld = world.eat(player1)
      assert(newWorld.getCellForPlayer(player1).get.food === 99)
    }
  }

  describe("#drink") {
    it("should decrement the water in the cell containing the given player") {
      assert(world.getCellForPlayer(player1).get.water === 100)
      val newWorld = world.drink(player1)
      assert(newWorld.getCellForPlayer(player1).get.water === 99)
    }
  }
}
