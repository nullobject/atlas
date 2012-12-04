package atlas

import akka.actor.{ActorSystem, Props}

object Main {
  val system = ActorSystem()

  def main(args: Array[String]) {
    var game = system.actorOf(Props[Game], name = "game")
    var server = system.actorOf(Props(new Server(game)), name = "server")
  }
}
