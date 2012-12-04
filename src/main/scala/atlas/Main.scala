package atlas

import akka.actor.{ActorSystem, Props}

object Main {
  val system = ActorSystem()

  def main(args: Array[String]) {
    val organism1 = Organism()

    val cell1 = Cell(position = (0, 0), organisms = Set(organism1))
    val cell2 = Cell(position = (1, 0))
    val cell3 = Cell(position = (0, 1))
    val cell4 = Cell(position = (1, 1))

    val world = World(cells = Set(cell1, cell2, cell3, cell4))

    var game = system.actorOf(Props(new Game(world)), name = "game")
    var server = system.actorOf(Props(new Server(game)), name = "server")
  }
}
