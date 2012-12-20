package atlas

import java.util.UUID
import scala.util.Random

/*
 * The world class represents the state of the world.
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
  def getSurroundingCells(cell: Cell) =
    Vector2.directions.flatMap { getAdjacentCell(cell, _) }

  // Returns the cells which contain organisims for the given player.
  def getCellsForPlayer(playerId: UUID) =
    cells.filter { _.organisms.find { _.playerId == playerId }.isDefined }

  def replaceCell(a: Cell, b: Cell) = copy(cells = cells - a + b)

  // Ticks the world state.
  def tick = tickCells.incrementAge

  def tickCells = copy(cells = cells.map(_.tick))

  def incrementAge = copy(age = age + 1)

  // Spawns the given organism into the world.
  def spawn(organism: Organism) = {
    if (organisms.contains(organism)) throw InvalidOperationException("Organism already spawned")
    val cell = Random.shuffle(cells).head
    val newCell = cell.addOrganism(organism)
    replaceCell(cell, newCell)
  }

  // Moves the given organism in the given direction.
  def move(organism: Organism, direction: Vector2) = {
    val fromOption = getCellForOrganism(organism)
    if (fromOption.isEmpty) throw InvalidOperationException("Unknown organism")
    val from = fromOption.get

    val toOption = getAdjacentCell(from, direction)
    if (toOption.isEmpty) throw InvalidOperationException("Invalid direction")
    val to = toOption.get

    val newFrom = from.removeOrganism(organism)
    val newTo = to.addOrganism(organism)
    replaceCell(from, newFrom).replaceCell(to, newTo)
  }

  // Decrements the food in the cell containing the given organism.
  def eat(organism: Organism) = {
    val cell = getCellForOrganism(organism).get
    if (cell.food == 0) throw InvalidOperationException("No food in cell")
    val newCell = cell.decrementFood.replaceOrganism(organism, organism.eat)
    replaceCell(cell, newCell)
  }

  // Decrements the water in the cell containing the given organism.
  def drink(organism: Organism) = {
    val cell = getCellForOrganism(organism).get
    if (cell.water == 0) throw InvalidOperationException("No water in cell")
    val newCell = cell.decrementWater.replaceOrganism(organism, organism.drink)
    replaceCell(cell, newCell)
  }
}

object World {
  case class InvalidOperationException(message: String) extends RuntimeException(message)
}
