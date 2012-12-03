package atlas

import language.postfixOps

import akka.actor.{ActorSystem, Props}
import scala.concurrent.duration._

object Main {
  val system = ActorSystem()

  def main(args: Array[String]) {
    var player = system.actorOf(Props[Router])
    import system.dispatcher
    system.scheduler.schedule(1 second, 1 second, player, Router.Tick)
  }
}
