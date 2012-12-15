package atlas

import java.util.UUID
import scala.util.Random

object World {
  case class InvalidOperationException(message: String) extends RuntimeException(message)
}

/*
 * The world class contains the world state.
 */
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
  def tick: World = copy(age = age + 1)

  // Spawns the given organism into the world.
  def spawn(organism: Organism): World = {
    if (organisms.contains(organism)) throw InvalidOperationException("Organism already spawned")
    val cell = Random.shuffle(cells).head
    val newCell = cell.copy(organisms = cell.organisms + organism)
    copy(cells = cells - cell + newCell)
  }

  // Moves the given organism in the given direction.
  def move(organism: Organism, direction: Vector2): World = {
    val fromOption = getCellForOrganism(organism)
    if (fromOption.isEmpty) throw InvalidOperationException("Unknown organism")
    val from = fromOption.get

    val toOption = getAdjacentCell(from, direction)
    if (toOption.isEmpty) throw InvalidOperationException("Invalid direction")
    val to = toOption.get

    val newFrom = from.copy(organisms = from.organisms - organism)
    val newTo = to.copy(organisms = to.organisms + organism)
    copy(cells = cells - from + newFrom - to + newTo)
  }

  // Decrements the food in the cell containing the given organism.
  def eat(organism: Organism): World = {
    val from = getCellForOrganism(organism).get
    if (from.food == 0) throw InvalidOperationException("No food in cell")
    val newFrom = from.copy(food = from.food - 1)
    copy(cells = cells - from + newFrom)
  }

  // Decrements the water in the cell containing the given organism.
  def drink(organism: Organism): World = {
    val from = getCellForOrganism(organism).get
    if (from.water == 0) throw InvalidOperationException("No water in cell")
    val newFrom = from.copy(water = from.water - 1)
    copy(cells = cells - from + newFrom)
  }
}
