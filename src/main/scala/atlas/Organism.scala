package atlas

import java.util.UUID

case class Organism(
  // The organism ID.
  id: UUID = UUID.randomUUID(),

  // The player ID.
  playerId: UUID = UUID.randomUUID(),

  // The organism genome.
  genome: Genome = Genome.empty,

  // The organism health.
  health: Int = 100
) {
  // Returns true if this organism is similar to the given organism.
  def isSimilar(that: Organism) =
    (this.genome similarity that.genome) > 0.9
}
