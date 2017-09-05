package com.newgenco.scalas.scalaista.actors

import akka.actor._
import akka.event.Logging

//Define the communication protocol
case class PingMessage(ct: Int)
case object PongMessage
case object StartMessage
case object StopMessage

class Ping(pong: ActorRef) extends Actor {
  var count = 0

  def incAndPrint { count += 1; println(s"ping $count")}

  override def receive = {

    case StartMessage => { incAndPrint; pong ! PingMessage(count) }

    case PongMessage => {
      incAndPrint
      if (count > 99) {
//        sender ! StopMessage
        sender ! PoisonPill
        println("Ping Stopped")
        context.stop(self)
      } else {
        sender ! PingMessage(count)
      }
    }
  }

}

class Pong extends Actor with ActorLogging {

  override def postStop(): Unit = log.info("IoT Application stopped")

  override def receive = {

    case PingMessage(ct) => { println(s"pong $ct "); sender ! PongMessage}

    case StopMessage => { println("pong stopped"); context.stop(self)}


  }
}



class Worker extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("IoT Application started")

  override def postStop(): Unit = log.info("IoT Application stopped")

  override def receive: Receive = {
    case "Hello" => log.info("Hello from actor ..."); context.stop(self);
    case _ => log.info("Unrecognized ...")
  }

}

object Runner extends App {

  val superior = ActorSystem("Worker-System").actorOf(Props(new Worker), "Worker-Superior")

  superior ! "Hello"

  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name= "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name="ping")
  ping ! StartMessage

}