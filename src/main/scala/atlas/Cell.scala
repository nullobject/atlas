package atlas

import java.util.UUID
import scala.util.Random
import scalaz.Lens

/**
 * The cell class represents the state of a cell.
 */
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

object Cell {
  val foodLens      = Lens.lensu[Cell, Int]((o, v) => o.copy(food = v), _.food)
  val waterLens     = Lens.lensu[Cell, Int]((o, v) => o.copy(water = v), _.water)
  val organismsLens = Lens.lensu[Cell, Set[Organism]]((o, v) => o.copy(organisms = v), _.organisms)

  // Decrements the food in the cell.
  def decrementFood = foodLens =>= { _ - 1 }

  // Decrements the water in the cell.
  def decrementWater = waterLens =>= { _ - 1 }

  // Adds the given organism to the cell.
  def addOrganism(a: Organism) = organismsLens =>= { _ + a }

  // Removes the given organism from the cell.
  def removeOrganism(a: Organism) = organismsLens =>= { _ - a }

  // Replaces the given organism in the cell.
  def replaceOrganism(a: Organism, b: Organism) = organismsLens =>= { _ - a + b }

  def findOrgansim(organismId: UUID)(cells: Set[Cell]) = cells.flatMap { _.organisms.find { _.id == organismId } }.headOption

  def findCellForOrganism(organism: Organism)(cells: Set[Cell]) = cells.find { _.organisms.contains(organism) }

  def findCellsForPlayer(playerId: UUID)(cells: Set[Cell]) = cells.filter { _.organisms.find { _.playerId == playerId }.isDefined }

  def findAdjacentCell(cell: Cell, direction: Vector2)(cells: Set[Cell]) = cells.find { _.position == cell.position + direction }

  def findSurroundingCells(cell: Cell)(cells: Set[Cell]) = {
    Vector2.directions.flatMap { findAdjacentCell(cell, _)(cells) }
  }

  // Ticks the state of the cell.
  def tick: Cell => Cell = organismsLens =>= { _.map(Organism.tick).filter(Organism.isAlive) }

  // Ticks the state of the cells.
  def tick(cells: Set[Cell]): Set[Cell] = cells.map(tick)

  def spawn(organism: Organism)(cells: Set[Cell]) = {
    val cell = Random.shuffle(cells).head
    val newCell = addOrganism(organism)(cell)
    cells - cell + newCell
  }

  def move(organism: Organism, direction: Vector2)(cells: Set[Cell]) = {
    val from = findCellForOrganism(organism)(cells).get
    val to = findAdjacentCell(from, direction)(cells).get
    val newFrom = removeOrganism(organism)(from)
    val newTo = addOrganism(organism)(to)
    cells - from - to + newFrom + newTo
  }

  def eat(organism: Organism)(cells: Set[Cell]) = {
    val cell = findCellForOrganism(organism)(cells).get
    val newCell = (decrementFood andThen replaceOrganism(organism, Organism.eat(organism)))(cell)
    cells - cell + newCell
  }

  def drink(organism: Organism)(cells: Set[Cell]) = {
    val cell = findCellForOrganism(organism)(cells).get
    val newCell = (decrementWater andThen replaceOrganism(organism, Organism.drink(organism)))(cell)
    cells - cell + newCell
  }
}
