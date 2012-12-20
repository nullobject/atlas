package atlas

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
) {
  // Ticks the cell state.
  def tick = tickOrganisms

  def tickOrganisms = copy(organisms = organisms.map(_.tick).filter(_.isAlive))

  def decrementFood = copy(food = food - 1)

  def decrementWater = copy(water = water - 1)

  def addOrganism(organism: Organism) = copy(organisms = organisms + organism)

  def removeOrganism(organism: Organism) = copy(organisms = organisms - organism)
}
