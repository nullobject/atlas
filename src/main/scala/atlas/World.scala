package atlas

import java.util.UUID
import scalaz._
import Scalaz._

/*
 * The world class represents the state of the world.
 */
case class World(
  // The cells in the world.
  cells: Set[Cell],

  // The age of the world.
  age: Int = 0
)

object World {
  case class InvalidOperationException(message: String) extends RuntimeException(message)

  val ageLens   = Lens.lensu[World, Int]((o, v) => o.copy(age = v), _.age)
  val cellsLens = Lens.lensu[World, Set[Cell]]((o, v) => o.copy(cells = v), _.cells)

  def replaceCell(a: Cell, b: Cell) = copy(cells = cells - a + b)

  // Ticks the world state.
  def tick = {
    val state = for {
      cells <- cellsLens %= { Cell.tick }
      age   <- ageLens += 1
      world <- get
    } yield world
    state.exec(_)
  }

  // Spawns the given organism into the world.
  def spawn(organism: Organism) = cellsLens =>= { Cell.spawn(organism) }

  // Moves the given organism in the given direction.
  def move(direction: Vector2)(organism: Organism) = cellsLens =>= { Cell.move(organism, direction) }

  // Decrements the food in the cell containing the given organism.
  def eat(organism: Organism) = cellsLens =>= { Cell.eat(organism) }

  // Decrements the water in the cell containing the given organism.
  def drink(organism: Organism) = cellsLens =>= { Cell.drink(organism) }
}
