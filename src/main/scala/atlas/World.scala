package atlas

import akka.actor.ActorRef
import VectorImplicits._

case class Cell(
  position: Vector2 = (0, 0),

  // The food available in this cell.
  food: Int = 100,

  // The water available in this cell.
  water: Int = 100,

  // The set of players currently occupying this cell.
  players: Set[ActorRef] = Set.empty
)

case class World(cells: Set[Cell] = Set.empty, age: Int = 0) {
  // Returns the cell at the given position.
  def getCellAtPosition(position: Vector2) =
    cells.find { _.position == position }

  // Returns the cell containing the given player.
  def getCellForPlayer(player: ActorRef) =
    cells.find { _.players.contains(player) }

  // Returns the cell adjacent to the given cell in the given direction.
  def getAdjacentCell(cell: Cell, direction: Vector2) =
    getCellAtPosition(cell.position + direction)

  // Moves the given player in the given direction.
  def move(player: ActorRef, direction: Vector2) = {
    val from = getCellForPlayer(player).get
    val to = getAdjacentCell(from, direction).get
    val newFrom = from.copy(players = from.players - player)
    val newTo = to.copy(players = to.players + player)
    copy(cells = cells - from + newFrom - to + newTo)
  }

  // Decrements the food in the cell containing the given player.
  def eat(player: ActorRef) = {
    val from = getCellForPlayer(player).get
    val newFrom = from.copy(food = from.food - 1)
    copy(cells = cells - from + newFrom)
  }

  // Decrements the water in the cell containing the given player.
  def drink(player: ActorRef) = {
    val from = getCellForPlayer(player).get
    val newFrom = from.copy(water = from.water - 1)
    copy(cells = cells - from + newFrom)
  }
}
