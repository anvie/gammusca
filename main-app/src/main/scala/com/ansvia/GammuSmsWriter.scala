package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:39 PM
 *
 */
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
