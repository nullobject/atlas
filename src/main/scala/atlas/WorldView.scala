package atlas

import spray.json._
import JsonFormats._

/**
 * A world view is a subset of the world from a player's perspective.
 *
 * A player can only ever see the cells immediately surrounding their current
 * location.
 */
case class WorldView(
  // The organisms controlled by the player.
  organisms: Set[Organism],

  // The cells visible to the player.
  cells: Set[Cell]
) {
  def serialize = this.toJson.compactPrint
}

object WorldView {
  def apply(world: World) = new WorldView(organisms = world.organisms, cells = world.cells)
}
