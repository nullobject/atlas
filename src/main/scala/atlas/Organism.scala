package atlas

import java.util.UUID

/**
 * The organism class represents a living organism. Organisms occupy cells in
 * the world.
 */
case class Organism(
  // The organism ID.
  id: UUID = UUID.randomUUID(),

  // The player ID.
  playerId: UUID = UUID.randomUUID(),

  // The organism genome.
  genome: Genome = Genome.empty,

  // The organism health.
  health: Int = 100,

  // The age of the organism.
  age: Int = 0,

  lastEat: Int = 0,

  lastDrink: Int = 0
) {
  // Returns true if the organism is alive.
  def isAlive = health > 0

  // Returns true if this organism is similar to the given organism.
  def isSimilar(that: Organism) = (this.genome similarity that.genome) > 0.9

  // Ticks the organism state.
  def tick = copy(age = age + 1)

  def eat = copy(health = health + 1, lastEat = age)

  def drink = copy(health = health + 1, lastDrink = age)
}
