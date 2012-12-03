package atlas

import akka.actor.{ActorSystem, Props}
import scala.concurrent.duration._
import scala.language.postfixOps

object Main {
  import system.dispatcher

  val system = ActorSystem()

  def main(args: Array[String]) {
    var game = system.actorOf(Props[Game], name = "game")
    var server = system.actorOf(Props(new Server(game)), name = "server")
    system.scheduler.schedule(1 second, 1 second, server, Server.Tick)
  }
}
