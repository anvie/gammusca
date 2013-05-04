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

trait GammuSmsReader extends ShellHelper {

  def pull():String = {
    try {
      exec("gammu","getallsms")
    }catch{
      case e:Exception =>
        e.printStackTrace()
        println(e.getMessage)
        ""
    }
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

      val data = pull()


      Thread.sleep(1000)
    }
  }

  def shutdown(){
    _stop = true
  }
}
