package akka.persistence

import akka.actor.ActorLogging
import akka.persistence._

object Counter{
  sealed trait Operation {
    val count: Int
  }

  case class Increment(override val count: Int) extends Operation
  case class Decrement(override val count: Int) extends Operation

  case class Cmd(op: Operation)
  case class Evt(op: Operation)

  case class State(count: Int)
}

class Counter extends PersistentActor with ActorLogging {
  import Counter._

  println("Starting.......................")

  override def persistenceId: String = "counter-example"

  var state: State = State(count = 0)

  def updateState(evt: Evt): Unit = evt match {
    case Evt(Increment(count)) =>
      state = State(count = state.count + count)
      takeSnapShot
    case Evt(Decrement(count)) =>
      state = State(count = state.count - count)
      takeSnapShot
  }

  val receiveRecover: Receive = {
    case evt: Evt =>
      println(s"Counter receive ${evt} on recovering mood")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Counte3r receive snapshot with data: ${snapshot} on recovering mood")
      state = snapshot
    case RecoveryCompleted =>
      println("Complete the recovery!!!")
  }

  val receiveCommand: Receive = {
    case cmd @ Cmd(op) =>
      println(s"Counter receive ${cmd}")
      persist(Evt(op)) { evt =>
        updateState(evt)
      }
    case "print" =>
      println(s"The Current state of counter is ${state}")

    case SaveSnapshotSuccess(metadata) =>
      println("Sucess Snapshot")

    case SaveSnapshotFailure(metadata, reason) =>
      println(s"Save snapshot failed")
  }

  def takeSnapShot = {
    if(state.count % 5 == 0){
      saveSnapshot(state)
    }
  }
  //override def recovery = Recovery.none
}

