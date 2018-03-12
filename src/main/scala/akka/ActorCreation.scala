package akka

import akka.MusicController.{Play, Stop}
import akka.MusicPlayer.{StartMusic, StopMusic}
import akka.actor.{Actor, ActorSystem, Props}

object MusicController {
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg

  def props = Props[MusicController]
}

class MusicController extends Actor {
  override def receive: Receive = {
    case Play =>
      println("Music Started ......")
    case Stop =>
      println("Music Stopped ......")
  }
}

object MusicPlayer {
  sealed trait PlayMsg
  case object StopMusic extends PlayMsg
  case object StartMusic extends PlayMsg

  def props = Props[MusicPlayer]
}

class MusicPlayer extends Actor {
  override def receive: Receive = {
    case StopMusic =>
      println("I don't want to stop the music")
    case StartMusic =>
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! Play
    case _ =>
      println("Unkown Message")
  }
}


object Creation extends App {
  val system = ActorSystem("creation")

  val player = system.actorOf(Props[MusicPlayer], "player")

  player ! StartMusic

  system.terminate()
}
