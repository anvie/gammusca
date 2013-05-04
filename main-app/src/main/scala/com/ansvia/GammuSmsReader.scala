package com.ansvia

import com.ansvia.commons.logging.Slf4jLogger

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:39 PM
 *
 */
trait GammuSmsReader extends ShellHelper with Slf4jLogger {

  protected var lastPullSmsCount = 0
  var gammuBin = "/usr/bin/gammu"

  def pullRaw():String = {
    try {
      exec(gammuBin,"getallsms")
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
    val smsSeq = pullAsString()
    val rv = smsSeq flatMap { str =>
      Sms.parseText(str)
    }
    lastPullSmsCount = rv.length
    debug("lastPullSmsCount: " + lastPullSmsCount)
    rv
  }

  def deleteAllSms(){
    try {
      exec(gammuBin, "deleteallsms", "1")
    }catch{
      case e:Exception =>
        e.printStackTrace()
        println(e.getMessage)
        ""
    }
  }
}
