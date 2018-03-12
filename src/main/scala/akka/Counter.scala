package akka

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSystem, Identify, PoisonPill, Props}

class Counter extends Actor {
  import akka.Counter._

  var count = 0

  override def receive: Receive = {
    case Inc(x) =>
      count += x
    case Dec(x) =>
      count -= x
  }
}

object Counter  {
  final case class Inc(num: Int)
  final case class Dec(num: Int)
}

class Watcher extends Actor {

  var counterRef: ActorRef = _

  val selection = context.actorSelection("/user/counter")

  selection ! Identify(None)

  override def receive: Receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor Reference for counter is ${ref}")
    case ActorIdentity(_, None) =>
      println("Actor doesnt live")
  }
}

object ActorPath extends App {
  val system = ActorSystem("Actors-Paths")

  val counter1 = system.actorOf(Props[Counter], "counter1")

  println(s"Actor Reference for counter1: ${counter1}")

  val counterSelection1 = system.actorSelection("counter1")

  println(s"Actor Selection for counter1: ${counterSelection1}")

  counter1 ! PoisonPill

  Thread.sleep(100)

  val counter2 = system.actorOf(Props[Counter], "counter1")

  println(s"Actor Reference for counter1: ${counter2}")

  val counterSelection2 = system.actorSelection("counter1")

  println(s"Actor Selection for counter1: ${counterSelection2}")

  counter1 ! PoisonPill

  Thread.sleep(100)

  val counter = system.actorOf(Props[Counter], "counter")


  val watcher = system.actorOf(Props[Watcher], "watcher")

  system.terminate()

}