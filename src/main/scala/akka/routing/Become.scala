package akka.routing

import akka.actor.{Actor, ActorSystem, FSM, Props, Stash}

case class User(username: String, email: String)

object UserStorage {
  //FSM State
  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  //FSM Data
  sealed trait Data
  case object EmptyData extends Data

  trait DBOperation

  object DBOperation {
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }

  case object Connect
  case object Disconnect
  case class Operation(dBOperation: DBOperation, user: Option[User])
}

class UserStorageFSM extends FSM[UserStorage.State, UserStorage.Data] with Stash {
  import UserStorage._

  //1. define start with
  startWith(Disconnected, EmptyData)
  //2. define state
  when(Disconnected){
    case Event(Connect, _) =>
      println("UserStorage Connected to DB")
      unstashAll()
      goto(Connected) using(EmptyData)
    case Event(_, _) =>
      stash()
      stay using(EmptyData)
  }

  when(Connected) {
    case Event(Disconnect, _) =>
      println("UserStorage disconnected from DB")
      goto(Disconnected) using EmptyData
    case Event(Operation(op, user), _) =>
      println(s"UserStorage receive ${op} operation to do in user: ${user}")
      stay using EmptyData
  }
  //3. initialize
  initialize()
}

class UserStorage extends Actor with Stash{
  import UserStorage._
  receive

  def connected: Actor.Receive = {
    case Disconnect =>
      println("User Storage Disconnect from DB")
      context.unbecome()
    case Operation(op, user) =>
      println(s"User Storage receive ${op} to do in user: ${user}")
  }

  def disconnected: Actor.Receive = {
    case Connect =>
      println(s"User Storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ => stash()
  }

  override def receive: Receive = disconnected
}

object BecomeHotswap extends App {
  import UserStorage._

  val system = ActorSystem("Hotswap-Become")

  val userStorage = system.actorOf(Props[UserStorageFSM], "userStorage")

  userStorage ! Operation(DBOperation.Create, Some(User("Admin", "admin@akka.com")))

  userStorage ! Connect

  userStorage ! Operation(DBOperation.Create, Some(User("Admin", "admin@akka.com")))

  userStorage ! Disconnect

  Thread.sleep(100)

  system.terminate()
}
