// An organism represents a genome expressed in a living entity.
case class Organism(genome: Genome, health: BigDecimal) {
  def tick: Organism = copy(health = health - 1)
}
