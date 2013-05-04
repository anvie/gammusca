package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:39 PM
 *
 */
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
      Sms.parseText(str)
    }
  }
}
