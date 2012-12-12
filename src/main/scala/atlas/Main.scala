package atlas

import akka.actor.Props
import akka.agent.Agent
import spray.can.server.SprayCanHttpServerApp

object Main extends App with SprayCanHttpServerApp {
  val size = 4
  val cells = for (x <- 0 until size; y <- 0 until size) yield Cell(position = (x, y))
  val world = World(cells = cells.toSet)
  val worldAgent = Agent(world)(system)
  var game = system.actorOf(Props(new Game(worldAgent)), name = "game")
  var server = system.actorOf(Props(new Server(game)), name = "server")

  newHttpServer(server) ! Bind(interface = "localhost", port = 8080)
}
