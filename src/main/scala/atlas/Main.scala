package atlas

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp

object Main extends App with SprayCanHttpServerApp {
  val size = 4
  val cells = for (x <- 0 until size; y <- 0 until size) yield Cell(position = (x, y))
  val world = World(cells = cells.toSet)
  var game = system.actorOf(Props(new Game(world)), name = "game")
  var server = system.actorOf(Props(new Server(game)), name = "server")

  newHttpServer(server) ! Bind(interface = "localhost", port = 8080)
}
