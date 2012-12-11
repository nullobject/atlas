package atlas

import java.util.UUID
import scala.util.{Failure, Success, Try, Random}
import spray.json._
import JsonFormats._

case class Cell(
  // The cell position.
  position: Vector2,

  // The food available in this cell.
  food: Int = 100,

  // The water available in this cell.
  water: Int = 100,

  // The set of organisms occupying this cell.
  organisms: Set[Organism] = Set.empty
)

object World {
  case class InvalidOperationException(message: String) extends RuntimeException(message)

  sealed trait Action

  object Action {
    // Do nothing.
    case object Idle extends Action

    // Spawns an organsim.
    case object Spawn extends Action

    // The given organism should consume a unit of food in the current cell.
    case class Eat(organismId: UUID) extends Action

    // The given organism should consume a unit of water in the current cell.
    case class Drink(organismId: UUID) extends Action

    // The given organism should move in the given direction.
    case class Move(organismId: UUID, direction: Vector2) extends Action

    def deserialize(value: String) = value.asJson.convertTo[Action]
  }
}

case class World(
  // The cells in the world.
  cells: Set[Cell],

  // The age of the world.
  age: Int = 0
) {
  import World._

  // Returns the organisms.
  def organisms = cells.flatMap { _.organisms }

  // Returns the organism with the given ID.
  def getOrgansim(id: UUID) =
    cells.flatMap { _.organisms.find { _.id == id } }.headOption

  // Returns the cell at the given position.
  def getCellAtPosition(position: Vector2) =
    cells.find { _.position == position }

  // Returns the cell containing the given organism.
  def getCellForOrganism(organism: Organism) =
    cells.find { _.organisms.contains(organism) }

  // Returns the cell adjacent to the given cell in the given direction.
  def getAdjacentCell(cell: Cell, direction: Vector2) =
    getCellAtPosition(cell.position + direction)

  /* // Returns the cells surrounding the given cell. */
  def getSurroundingCells(cell: Cell) = {
    val directions = Set(Direction.N, Direction.NE, Direction.E, Direction.SE, Direction.S, Direction.SW, Direction.W, Direction.NW)
    directions.flatMap { getAdjacentCell(cell, _) }
  }

  // Returns the cells which contain organisims for the given player.
  def getCellsForPlayer(playerId: UUID) =
    cells.filter { _.organisms.find { _.playerId == playerId }.isDefined }

  // Ticks the world state.
  def tick: Try[World] = Success(copy(age = age + 1))

  // Spawns the given organism into the world.
  def spawn(organism: Organism): Try[World] = {
    if (organisms.contains(organism)) return Failure(InvalidOperationException("Organism already spawned"))
    val cell = Random.shuffle(cells).head
    val newCell = cell.copy(organisms = organisms + organism)
    Success(copy(cells = cells - cell + newCell))
  }

  // Moves the given organism in the given direction.
  def move(organism: Organism, direction: Vector2): Try[World] = {
    val fromOption = getCellForOrganism(organism)
    if (fromOption.isEmpty) return Failure(InvalidOperationException("Unknown organism"))
    val from = fromOption.get

    val toOption = getAdjacentCell(from, direction)
    if (toOption.isEmpty) return Failure(InvalidOperationException("Invalid direction"))
    val to = toOption.get

    val newFrom = from.copy(organisms = from.organisms - organism)
    val newTo = to.copy(organisms = to.organisms + organism)
    Success(copy(cells = cells - from + newFrom - to + newTo))
  }

  // Decrements the food in the cell containing the given organism.
  def eat(organism: Organism): Try[World] = {
    val from = getCellForOrganism(organism).get
    if (from.food == 0) return Failure(InvalidOperationException("No food in cell"))
    val newFrom = from.copy(food = from.food - 1)
    Success(copy(cells = cells - from + newFrom))
  }

  // Decrements the water in the cell containing the given organism.
  def drink(organism: Organism): Try[World] = {
    val from = getCellForOrganism(organism).get
    if (from.water == 0) return Failure(InvalidOperationException("No water in cell"))
    val newFrom = from.copy(water = from.water - 1)
    Success(copy(cells = cells - from + newFrom))
  }
}
