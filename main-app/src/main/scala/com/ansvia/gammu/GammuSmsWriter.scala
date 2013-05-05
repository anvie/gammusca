package com.ansvia.gammu

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:39 PM
 *
 */
trait GammuSmsWriter extends ShellHelper {

  protected val backend:GammuStorageBackend

  var gammuBin:String // = "/usr/bin/gammu"

  protected def error(str:String)
  protected def info(str:String)


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
    val nmsg = if (msg.length > 160)
      msg.substring(0, 160).trim
    else
      msg.trim
    validateMsg(nmsg)

    try {
      exec(gammuBin, "sendsms", "TEXT", nn, "-len", "160", "-text", nmsg)
    }catch{
      case e:Exception =>
        error("Gagal kirim sms ke: " + phoneNumber + ", pesan: " + nmsg)
        e.printStackTrace()
        // backup sms to draft
        info("backup last failed to send sms into Draft")
        backend.push(Sms("",phoneNumber,SmsStatus.Unread,"","",nmsg), Folder.Draft)
    }
  }
}
