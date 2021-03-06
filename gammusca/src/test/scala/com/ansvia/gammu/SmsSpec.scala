package com.ansvia.gammu

import org.specs2.Specification

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 9:53 PM
 *
 */
class SmsSpec extends Specification {

  def is =
    sequential ^
      "Sms should" ^
      p ^
        "parse text" ! trees.parseText ^
        "parse json" ! trees.parseJson ^
        "serialize to json" ! trees.toJson ^
    end

  object trees {
    private val text = """SMS message
                         |SMSC number          : "+62816124"
                         |Sent                 : Sat 04 May 2013 07:13:16 PM  +0700
                         |Coding               : Default GSM alphabet (no compression)
                         |Remote number        : "+6285717997711"
                         |Status               : UnRead
                         |
                         |1 200, 2 350, 3 105,rusak 10
                         |
                         |
                         |
                         |2 SMS parts in 2 SMS sequences""".stripMargin
    private val jsonText =
      """
        |{
        |"fromNumber": "+6285717997711",
        |"toNumber": "",
        |"status": "Unread",
        |"sent": "Sat 04 May 2013 07:13:16 PM  +0700",
        |"smsc": "+62816124",
        |"message": "1 200, 2 350, 3 105,rusak 10"
        |}
      """.stripMargin

    def parseText = {
      val s = Sms("+6285717997711", "", SmsStatus.Unread, "Sat 04 May 2013 07:13:16 PM  +0700", "+62816124", "1 200, 2 350, 3 105,rusak 10")
      Sms.parseText(text) must beEqualTo(Some(s))
    }

    def parseJson = {
      val s = Sms("+6285717997711", "", SmsStatus.Unread, "Sat 04 May 2013 07:13:16 PM  +0700", "+62816124", "1 200, 2 350, 3 105,rusak 10")
      Sms.parseJson(jsonText) must beEqualTo(Some(s))
    }

    def toJson = {
      val s = Sms("+6285717997711", "", SmsStatus.Unread, "Sat 04 May 2013 07:13:16 PM  +0700",
        "+62816124", "1 200, 2 350, 3 105,rusak 10").toJson
      s must beEqualTo(jsonText.trim)
    }

  }
}
