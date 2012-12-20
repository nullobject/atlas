package atlas

import java.util.UUID
import scalaz._
import Scalaz._

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
  // Returns true if this organism is similar to the given organism.
  def isSimilar(that: Organism) = (this.genome similarity that.genome) > 0.9
}

object Organism {
  val ageLens       = Lens.lensu[Organism, Int]((o, v) => o.copy(age = v), _.age)
  val healthLens    = Lens.lensu[Organism, Int]((o, v) => o.copy(health = v), _.health)
  val lastEatLens   = Lens.lensu[Organism, Int]((o, v) => o.copy(lastEat = v), _.lastEat)
  val lastDrinkLens = Lens.lensu[Organism, Int]((o, v) => o.copy(lastDrink = v), _.lastDrink)

  // Decrements the health of the organism.
  def decrementHealth = healthLens =>= { _ - 1 }

  // Increments the age of the organism.
  def incrementAge = ageLens =>= { _ + 1 }

  // Returns true if the organism is alive.
  def isAlive(organism: Organism) = organism.health > 0

  // Ticks the organism state.
  def tick(organism: Organism) = {
    if ((organism.age - organism.lastEat) % organism.genome("EatFrequency") == 0)
      (incrementAge andThen decrementHealth)(organism)
    else if ((organism.age - organism.lastDrink) % organism.genome("DrinkFrequency") == 0)
      (incrementAge andThen decrementHealth)(organism)
    else
      (incrementAge)(organism)
  }

  def eat = {
    val state = for {
      age <- ageLens
      _ <- lastEatLens := age
      _ <- healthLens += 1
      organism <- get
    } yield organism
    state.exec(_)
  }

  def drink = {
    val state = for {
      age <- ageLens
      _ <- lastDrinkLens := age
      _ <- healthLens += 1
      organism <- get
    } yield organism
    state.exec(_)
  }
}
