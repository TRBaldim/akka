package akka.cluster

import akka.actor.Actor

class Worker extends Actor{
  import Worker._

  override def receive: Receive = {
    case msg: Work =>
      println(s"I received a message and my ActorRef: ${self}")
  }
}

object Worker {
  case class Work(message: String)
}
