package akka.cluster

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.stuffs.Add
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

import scala.util.Random

class FrontendRouter extends Actor {
  import context.dispatcher
  val backend = context.actorOf(FromConfig.props(), name = "backendRouter")

  context.system.scheduler.schedule(3.seconds, 3.seconds, self,
    Add(Random.nextInt(100), Random.nextInt(100)))

  override def receive: Receive = {
    case addOps: Add =>
      println("Frontend: I'll forward add operation to backend node to handle id.")
      backend forward addOps
  }
}

object FrontendRouter {
  private var _frontend: ActorRef = _

  def initiate() = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [frontend]")
      .withFallback(ConfigFactory.load("Frontend"))

    val system = ActorSystem("ClusterSystem", config)

    _frontend = system.actorOf(Props[FrontendRouter], name = "frontend")
  }

  def getFrontend = _frontend

}
