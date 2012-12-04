package atlas

import java.util.UUID

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

case class World(cells: Set[Cell], age: Int = 0) {
  // Returns the organisms.
  def organisms = cells.flatMap { _.organisms }

  // Returns the organism with the given ID.
  def getOrganismById(id: UUID) =
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

  // Ticks the world state.
  def tick() = copy(age = age + 1)

  // Moves the given organism in the given direction.
  def move(organism: Organism, direction: Vector2) = {
    val from = getCellForOrganism(organism).get
    val to = getAdjacentCell(from, direction).get
    val newFrom = from.copy(organisms = from.organisms - organism)
    val newTo = to.copy(organisms = to.organisms + organism)
    copy(cells = cells - from + newFrom - to + newTo)
  }

  // Decrements the food in the cell containing the given organism.
  def eat(organism: Organism) = {
    val from = getCellForOrganism(organism).get
    val newFrom = from.copy(food = from.food - 1)
    copy(cells = cells - from + newFrom)
  }

  // Decrements the water in the cell containing the given organism.
  def drink(organism: Organism) = {
    val from = getCellForOrganism(organism).get
    val newFrom = from.copy(water = from.water - 1)
    copy(cells = cells - from + newFrom)
  }
}
