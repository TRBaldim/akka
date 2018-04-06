package akka.cluster

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.cluster.stuffs.{Add, BackendRegistration}
import com.typesafe.config.ConfigFactory

import scala.util.Random

class Frontend extends Actor {

  var backends = IndexedSeq.empty[ActorRef]

  override def receive: Receive = {
    case Add if backends.isEmpty =>
      println("Service unavailable, cluster doesn't have backend node.")
    case addOps: Add =>
      println("Frontend: I'll forward add operation to backend node to handle id.")
      backends(Random.nextInt(backends.size)) forward addOps
    case BackendRegistration if !(backends.contains(sender())) =>
      backends = backends :+ sender()
      context watch sender()
    case Terminated(a) =>
      backends = backends.filterNot(_ == a)
  }
}

object Frontend {
  private var _frontend: ActorRef = _

  def initiate() = {
    val config = ConfigFactory.load().getConfig("Frontend")

    val system = ActorSystem("ClusterSystem", config)

    _frontend = system.actorOf(Props[Frontend], name = "frontend")
  }

  def getFrontend = _frontend
}
