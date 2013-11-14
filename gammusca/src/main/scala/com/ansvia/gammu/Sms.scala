package com.ansvia.gammu

import scala.util.parsing.json.JSON
import scala.collection.mutable.ArrayBuffer

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
case class Sms(fromNumber:String, toNumber:String, status:SmsStatus, sent:String, smsc:String, message:String) {
    override def hashCode() = (fromNumber + toNumber + sent + smsc + message).hashCode

    def toJson = Sms.convertToJson(this)
}


object Sms {
    def parseText(text:String):Option[Sms] = {
        var ar = new ArrayBuffer[String]
        val sd = text.split("\n")
        sd slice (0, 7) foreach { line =>
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
        if (sd.length == 1)
            return None

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
        val message = messages.mkString("").trim
        //    println(ar)
        val far = ar.result().toIndexedSeq
//        println(far.mkString("\n"))
        val status = {
            val idx = far.length - 1
            far(idx) match {
                case "UnRead" => SmsStatus.Unread
                case "Read" => SmsStatus.Read
            }
        }
        Some(Sms(far(3), "", status, far(1), far(0), message))
    }
    def parseJson(text:String):Option[Sms] = {
        val result = JSON.parseFull(text)
        result match {
            case Some(d:Map[String, String]) =>
                Some(Sms(d.getOrElse("fromNumber", ""),
                    d.getOrElse("toNumber", ""),
                    d.getOrElse("status", "") match {
                        case "Unread" => SmsStatus.Unread
                        case "Read" => SmsStatus.Read
                    }, d.getOrElse("sent", ""),
                    d.getOrElse("smsc", ""),
                    d.getOrElse("message", "")
                ))
            case None =>
                None
        }
    }
    def convertToJson(sms:Sms):String = {
        """{
          |"fromNumber": "%s",
          |"toNumber": "%s",
          |"status": "%s",
          |"sent": "%s",
          |"smsc": "%s",
          |"message": "%s"
          |}
        """.stripMargin.format(sms.fromNumber, sms.toNumber, sms.status match {
            case SmsStatus.Read => "Read"
            case SmsStatus.Unread => "Unread"
        },sms.sent, sms.smsc, sms.message).trim
    }
}

