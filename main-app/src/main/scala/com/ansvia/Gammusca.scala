package com.ansvia


import com.ansvia.commons.logging.Slf4jLogger


object Gammusca {

    def main(args:Array[String]){



    }

}


trait ShellHelper {
  import scala.sys.process._

  def exec(cmds:String*):String = {
    cmds !!
  }

}

case class Sms(fromNumber:String, status:SmsStatus, sent:String, smsc:String, message:String)

object Sms {
  def parseText(text:String):Sms = {
    var ar = Array.empty[String]
    val sd = text.split("\n")
    sd slice (0, 4) foreach { line =>
      val d = line.split(":")
      if (d.length > 1){
        ar +:= d(1).trim
      }
    }
    val sdIter = sd.toIterator
    sdIter.next()
    sdIter.next()
    sdIter.next()
    sdIter.next()
    sdIter.next()
    var messages = Array.empty[String]
    while(sdIter.hasNext){
      val t = sdIter.next()
      if ( !("""\d+ SMS parts in \d+ SMS sequences""".r.pattern.matcher(t).matches()) ){
        messages +:= t.trim + "\n"
      }
    }
    val message = messages.reduceLeftOption(_ + _).getOrElse("")
    val status = ar(4) match {
      case "UnRead" => SmsStatus.Unread
      case "Read" => SmsStatus.Read
    }
    Sms(ar(3), status, ar(1), ar(0), message)
  }
}

trait SmsStatus
object SmsStatus {
  object Unread extends SmsStatus
  object Read extends SmsStatus
}

trait GammuSmsReader extends ShellHelper {

  def pullRaw():String = {
    try {
      exec("gammu","getallsms")
    }catch{
      case e:Exception =>
        e.printStackTrace()
        println(e.getMessage)
        ""
    }
  }

  def pullAsString():List[String] = {
    val data = pullRaw().trim
    if (data.length > 0){
      GammuUtil.smsesSplit(data)
    }else{
      List.empty[String]
    }
  }

  def pull():List[Sms] = {
    pullAsString() map { str =>

    }
  }
}

object GammuUtil {
  def smsesSplit(text:String):List[String] = {
    text.split("""Location \d+, folder "Inbox", SIM memory, Inbox folder""").toList.map(_.trim).filter(_.length > 1)
  }
}

trait GammuSmsWriter extends ShellHelper {
  def normalizeNumber(number:String):String = {
    if (number.startsWith("0"))
      "+62" + number.substring(1)
    else
      number
  }
  def validateNumber(number:String){
    if (!number.startsWith("+"))
      throw new Exception("Invalid phone number " + number + ", should starts with +, eg: +6281234...")
    if (number.length < 3)
      throw new Exception("Invalid phone number, less than 3 digit")
    if (number.length > 40)
      throw new Exception("Invalid phone number, more than 40 digit")
  }
  def validateMsg(msg:String){
    if (msg.length < 1)
      throw new Exception("Invalid message, less than 1 characters.")
    if (msg.length > 160)
      throw new Exception("Invalid message, more than 160 characters.")
  }
  def send(phoneNumber:String, msg:String){
    val nn = normalizeNumber(phoneNumber)
    validateNumber(nn)
    val nmsg = msg.trim
    validateMsg(nmsg)

    exec("gammu", "sendsms", "TEXT", nn, "-text", nmsg)
  }
}
trait Gammu extends GammuSmsReader with GammuSmsWriter


class GammuDaemon extends Thread with Gammu with Slf4jLogger {

  private var _stop = false

  override def run() {

    while (!_stop){

      info("reading smses from device...")

      val data = pullRaw()


      Thread.sleep(1000)
    }
  }

  def shutdown(){
    _stop = true
  }
}
