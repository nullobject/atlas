package atlas

import spray.json._
import java.util.UUID
import JsonFormats._

/**
 * A world view is a subset of the world from a player's perspective.
 *
 * A player can only ever see the cells immediately surrounding their current
 * location.
 */
case class WorldView(
  // The cells visible to the player.
  cells: Set[Cell],

  // The age of the world.
  age: Int = 0
) {
  def serialize = this.toJson.compactPrint
}

object WorldView {
  def scopeToPlayer(playerId: UUID, world: World) = {
    val playerCells = Cell.findCellsForPlayer(playerId)(world.cells)
    val surroundingCells = playerCells.flatMap { cell => Cell.findSurroundingCells(cell)(world.cells) }
    val cells = playerCells | surroundingCells
    WorldView(cells = cells, age = world.age)
  }
}
