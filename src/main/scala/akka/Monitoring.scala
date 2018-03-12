package akka

import akka.actor.{ActorRef, ActorSystem, Props, Actor, Terminated}

class Ares(athena: ActorRef) extends Actor {
  override def preStart(): Unit = {
    context.watch(athena)
  }

  override def postStop(): Unit = {
    println("Ares postStop...")
  }

  override def receive: Receive = {
    case Terminated =>
      context.stop(self)
  }
}

class Athena extends Actor {
  def receive = {
    case msg =>
      println(s"Athena received ${msg}")
      context.stop(self)
  }
}


object Monitoring extends App {
  val system = ActorSystem("MonitorSystem")

  private val athena: ActorRef = system.actorOf(Props[Athena], "athena")
  private val ares: ActorRef = system.actorOf(Props(new Ares(athena)), "ares")

  athena ! "hello"
  athena ! "hello"
  system.terminate()

}
