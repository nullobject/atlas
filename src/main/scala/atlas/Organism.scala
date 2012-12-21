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
  health: Long = 100,

  // The age of the organism.
  age: Long = 0,

  lastEat: Long = 0,

  lastDrink: Long = 0,

  lastReproduce: Long = 0
) {
  // Returns true if the organism is alive.
  def isAlive = health > 0

  // Returns true if this organism is similar to the given organism.
  def isSimilar(that: Organism) = (this.genome similarity that.genome) > 0.9

  // Ticks the organism state.
  def tick = incrementAge.applyHunger.applyThirst

  def eat = incrementHealth.copy(lastEat = age)

  def drink = incrementHealth.copy(lastDrink = age)

  def incrementAge = copy(age = age + 1)

  def incrementHealth = copy(health = health + 1)

  def decrementHealth(n: Long) = copy(health = health - n)

  def applyHunger = {
    if (age - lastEat >= genome("EatFrequency")) {
      val n = math.pow(2.0, age - lastEat - genome("EatFrequency")).round
      decrementHealth(n)
    } else
      this
  }

  def applyThirst = {
    if (age - lastDrink >= genome("DrinkFrequency")) {
      val n = math.pow(2.0, age - lastDrink - genome("DrinkFrequency")).round
      decrementHealth(n)
    } else
      this
  }

  def reproduce = {
    if (age - lastReproduce >= genome("ReproduceFrequency"))
      Set(copy(lastReproduce = age), Organism(playerId = playerId, genome = genome.mutate))
    else
      Set(this)
  }
}
