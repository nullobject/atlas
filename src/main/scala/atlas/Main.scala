package atlas

import akka.actor.{ActorSystem, Props}

object Main {
  val system = ActorSystem()

  def main(args: Array[String]) {
    var world = World(cells = Set.empty)
    var game = system.actorOf(Props(new Game(world)), name = "game")
    var server = system.actorOf(Props(new Server(game)), name = "server")
  }
}
