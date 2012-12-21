package atlas

/**
 * The cell class represents the state of a cell.
 */
case class Cell(
  // The cell position.
  position: Vector2,

  // The food available in this cell.
  food: Long = 100,

  // The water available in this cell.
  water: Long = 100,

  // The set of organisms occupying this cell.
  organisms: Set[Organism] = Set.empty
) {
  // Ticks the cell state.
  def tick = tickOrganisms.reproduceOrganisms

  def tickOrganisms = copy(organisms = organisms.map(_.tick).filter(_.isAlive))

  def reproduceOrganisms = copy(organisms = organisms.flatMap(_.reproduce))

  def decrementFood = copy(food = food - 1)

  def decrementWater = copy(water = water - 1)

  def addOrganism(organism: Organism) = copy(organisms = organisms + organism)

  def removeOrganism(organism: Organism) = copy(organisms = organisms - organism)

  def replaceOrganism(a: Organism, b: Organism) = copy(organisms = organisms - a + b)
}
