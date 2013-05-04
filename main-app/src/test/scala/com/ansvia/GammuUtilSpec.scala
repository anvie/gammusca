package com.ansvia

import org.specs2.Specification

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 9:35 PM
 *
 */
class GammuUtilSpec extends Specification {

  def is =
    sequential ^
    "GammuUtil should" ^
    p ^
      "split sms text sequences" ! trees.split  ^
    end

  object trees {
    val data = """Location 1, folder "Inbox", SIM memory, Inbox folder
                 |SMS message
                 |SMSC number          : "+62816124"
                 |Sent                 : Sat 04 May 2013 01:13:52 AM  +0700
                 |Coding               : Default GSM alphabet (no compression)
                 |Remote number        : "+6285778019564"
                 |Status               : UnRead
                 |
                 |TOKO BLACKBERRY  88
                 |DISKON 40%
                 |DAKOTA 2,7Jt
                 |S.NOTE II
                 |IPAD 4 32GB
                 |IPHONE 5 32GB
                 |KAMERA 500D
                 |PIN 29378D48
                 |Hbu: 0856-9661-6662
                 |www.toko-blackberry88.blogspot.com
                 |
                 |Location 2, folder "Inbox", SIM memory, Inbox folder
                 |SMS message
                 |SMSC number          : "+62816124"
                 |Sent                 : Sat 04 May 2013 07:13:16 PM  +0700
                 |Coding               : Default GSM alphabet (no compression)
                 |Remote number        : "+6285717997788"
                 |Status               : UnRead
                 |
                 |1 200, 2 350, 3 105,rusak 10
                 |
                 |
                 |
                 |2 SMS parts in 2 SMS sequences""".stripMargin.trim
    def split = {
      val s = GammuUtil.smsesSplit(data)
      println(s)
      (s.length must beEqualTo(2)) and
        (s(0).startsWith("SMS message") must beTrue) and
        (s(0).startsWith("SMS message") must beTrue)
    }
  }

}
