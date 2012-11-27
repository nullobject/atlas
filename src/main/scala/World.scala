import akka.actor.ActorRef

case class Cell(
  position: Tuple2[Int, Int],

  // The organisms currently occupying this cell.
  organisms: Set[ActorRef] = Set.empty,

  // The food available in this cell.
  food: Int = 100,

  // The water available in this cell.
  water: Int = 100
)

case class World(cells: Set[Cell] = Set.empty, age: Int = 0) {
  // Returns the cell at the given position.
  def getCellAtPosition(position: Game.Vector2) =
    cells.find { _.position == position }

  // Returns the cell containing the given organism.
  def getCellForOrgansim(organism: ActorRef) =
    cells.find { _.organisms.contains(organism) }

  // Returns the cell adjacent to the given cell in the given direction.
  def getAdjacentCell(cell: Cell, direction: Game.Vector2) = {
    val position = (cell.position._1 + direction._1, cell.position._2 + direction._2)
    getCellAtPosition(position)
  }

  // Moves the given organism in the given direction.
  def move(organism: ActorRef, direction: Game.Vector2) = {
    val from = getCellForOrgansim(organism).get
    val to = getAdjacentCell(from, direction).get
    val newFrom = from.copy(organisms = from.organisms - organism)
    val newTo = to.copy(organisms = to.organisms + organism)
    copy(cells = cells - from + newFrom - to + newTo)
  }

  // Decrements the food in the cell containing the given organism.
  def eat(organism: ActorRef) = {
    val from = getCellForOrgansim(organism).get
    val newFrom = from.copy(food = from.food - 1)
    copy(cells = cells - from + newFrom)
  }

  // Decrements the water in the cell containing the given organism.
  def drink(organism: ActorRef) = {
    val from = getCellForOrgansim(organism).get
    val newFrom = from.copy(water = from.water - 1)
    copy(cells = cells - from + newFrom)
  }
}
