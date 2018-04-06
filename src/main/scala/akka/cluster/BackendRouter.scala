package akka.cluster

import akka.actor.{Actor, ActorSystem, Props}
import akka.cluster.stuffs.Add
import com.typesafe.config.ConfigFactory

class BackendRouter extends Actor {

  override def receive: Receive = {
    case Add(num1, num2) =>
      println(s"I'm a backend with path: ${self} and I receive add oeprations")
  }

}

object BackendRouter {
  def initiate(port: Int): Unit = {
    println("Vai?")
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
      .withFallback(ConfigFactory.load("loadbalancer"))

    val system = ActorSystem("ClusterSystem", config)

    val Backend = system.actorOf(Props[BackendRouter], name = "backend")
  }
}
