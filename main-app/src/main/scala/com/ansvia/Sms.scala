package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
case class Sms(fromNumber:String, status:SmsStatus, sent:String, smsc:String, message:String) {
  override def hashCode() = (fromNumber + sent + smsc + message).hashCode
}


object Sms {
  def parseText(text:String):Sms = {
    var ar = List.empty[String]
    val sd = text.split("\n")
    sd slice (0, 6) foreach { line =>
      val d = line.split(":")
      if (d.length > 1){
        if (d.length > 2){
          ar :+= d.slice(1,d.length).reduceLeftOption(_ + ":" + _).getOrElse("").trim.replaceAll("""^"+|"+$""", "").trim
        }else{
          ar :+= d(1).trim.replaceAll("""^"+|"+$""", "").trim
        }
      }
    }
    val sdIter = sd.toIterator
    sdIter.next()
    sdIter.next()
    sdIter.next()
    sdIter.next()
    sdIter.next()
    sdIter.next()
    var messages = Array.empty[String]
    while(sdIter.hasNext){
      val t = sdIter.next().trim
      if ( !("""\d+ SMS parts in \d+ SMS sequences""".r.pattern.matcher(t).matches()) && t.length > 0 ){
        messages +:= t + " "
      }
    }
    val message = messages.reduceLeftOption(_ + _).getOrElse("").trim
    //    println(ar)
    val status = ar(4) match {
      case "UnRead" => SmsStatus.Unread
      case "Read" => SmsStatus.Read
    }
    Sms(ar(3), status, ar(1), ar(0), message)
  }
}

