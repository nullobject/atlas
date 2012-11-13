object World {
  sealed trait State
  case object Idle extends State

  case class Data(age: BigDecimal)
}

case class World(organisms: Seq[Organism], state: World.State, data: World.Data) {
  import World._

  def tick: World = copy(
    organisms = organisms.map(_.tick),
    data      = Data(data.age + 1)
  )
}
