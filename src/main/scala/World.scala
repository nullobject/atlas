case class World(organisms: Seq[Organism], age: BigDecimal) {
  def tick: World = copy(
    organisms = organisms.map(_.tick),
    age       = age + 1
  )
}
