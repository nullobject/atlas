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
  age: Long = 0
) {
  def serialize = this.toJson.compactPrint
}

object WorldView {
  def scopeToPlayer(playerId: UUID, world: World) = {
    val playerCells = world.getCellsForPlayer(playerId)
    val surroundingCells = playerCells.flatMap { world.getSurroundingCells }
    val cells = playerCells | surroundingCells
    WorldView(cells = cells, age = world.age)
  }
}
