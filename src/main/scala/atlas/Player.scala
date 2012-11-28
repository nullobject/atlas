package atlas

import java.util.UUID

case class Player(
  id: UUID = UUID.randomUUID(),
  genome: Genome = Genome.empty,
  health: Int = 100
)
