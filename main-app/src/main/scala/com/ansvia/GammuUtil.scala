package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
object GammuUtil {
  def smsesSplit(text:String):List[String] = {
    text.split("""Location \d+, folder "Inbox", SIM memory, Inbox folder""").toList.map(_.trim).filter(_.length > 1)
  }
}
