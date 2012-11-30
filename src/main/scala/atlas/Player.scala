package atlas

import java.util.UUID

case class Player(
  // The player ID.
  id: UUID = UUID.randomUUID(),

  // The player genome.
  genome: Genome = Genome.empty,

  // The player health.
  health: Int = 100
) {
   // Returns true if this player is similar to the given player.
  def isSimilar(that: Player) =
    (this.genome similarity that.genome) > 0.9
}
