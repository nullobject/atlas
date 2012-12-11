package atlas

import java.util.UUID

object Player {
  case class Intention(playerId: UUID, action: World.Action)
}
