import akka.actor.{Actor, ActorRef, FSM}

case class Cell(
  position: Tuple2[Int, Int],

  // The organisms currently occupying this cell.
  organisms: Set[ActorRef] = Set.empty,

  // The food available in this cell.
  food: Int = 0,

  // The water available in this cell.
  water: Int = 0
)

object World {
  type Vector2 = Tuple2[Int, Int]
  object N extends Vector2( 0, -1)
  object S extends Vector2( 0,  1)
  object E extends Vector2( 1,  0)
  object W extends Vector2(-1,  0)

  sealed trait State
  case object Idle extends State

  case class Data(cells: Set[Cell] = Set.empty, age: Int = 0) {
    val sqrtCellsLength = scala.math.sqrt(cells.size).toInt

    // Returns the cell at the given position.
    def getCellAtPosition(position: Vector2) = Some(cells.toSeq((position._2 * sqrtCellsLength) + position._1))

    // Returns the cell which contains the given organism.
    def getCellForOrgansim(organism: ActorRef) = cells.find { _.organisms.contains(organism) }

    // Returns the cell adjacent to the given cell in the given direction.
    def getAdjacentCell(cell: Cell, direction: Vector2) = {
      val position = (cell.position._1 + direction._1, cell.position._2 + direction._2)
      getCellAtPosition(position)
    }
  }

  sealed trait Message

  // Move the organism in the given direction.
  case class Move(organism: ActorRef, direction: Vector2) extends Message

  // Consume a unit of food in the current cell.
  case class Eat(organism: ActorRef) extends Message

  // Consume a unit of water in the current cell.
  case class Drink(organism: ActorRef) extends Message

  def moveOrganism(data: Data, organism: ActorRef, direction: Vector2) = {
    val source = data.getCellForOrgansim(organism).get
    val destination = data.getAdjacentCell(source, direction).get
    val newSource = source.copy(organisms = source.organisms - organism)
    val newDestination = destination.copy(organisms = destination.organisms + organism)
    data.copy(cells = data.cells - source - destination + newSource + newDestination)
  }
}

/*
 * A world represents a set of cells.
 */
class World extends Actor with FSM[World.State, World.Data] {
  import World._

  startWith(Idle, Data())
  when(Idle) {
    case Event(Move(organism, direction), data) => {
      stay using moveOrganism(data, organism, direction)
    }
  }
  initialize
}
