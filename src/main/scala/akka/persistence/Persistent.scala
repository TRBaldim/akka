package akka.persistence

import akka.actor.{ActorSystem, Props}

object Persistent extends App {
  import Counter._

  val system =  ActorSystem("persistent-actors")

  val counter = system.actorOf(Props[Counter])

  counter ! Cmd(Increment(1))

  counter ! Cmd(Increment(2))

  counter ! Cmd(Decrement(0))

  counter ! "print"

  Thread.sleep(1000)

  system.terminate()
}
