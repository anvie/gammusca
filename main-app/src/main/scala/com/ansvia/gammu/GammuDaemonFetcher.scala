package com.ansvia.gammu

import com.redis.RedisClient
import com.ansvia.commons.logging.Slf4jLogger

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
class GammuDaemonFetcher extends Thread with GammuSmsReader with Slf4jLogger {

  var gammuBin = "/usr/bin/gammu"
  protected lazy val backend = new GammuRedisStorage("localhost", 6379)
  private var _stop = false
//  private var smsProcessor:GammuSmsProcessor = _

  override def run() {

    var emptyCount = 0

    while (!_stop){

      info("reading smses from device...")

      // dapatkan data dari sim card
      // lalu masukkan ke storage
      val smses = pull()
      if (smses.filter(_.status == SmsStatus.Unread).length == 0)
        emptyCount += 1
      else
        emptyCount = 0

      smses foreach { sms =>
        if (sms.status != SmsStatus.Read){ // hanya untuk sms yang belum pernah dibaca.
          backend.push(sms, Folder.Inbox)
          reply(sms.fromNumber,"sms diterima, panjang karakter: " + sms.message.length)
        }
      }

      if (lastPullSmsCount >= 20 && emptyCount > 5){
        // clear up sim inbox memory
        debug("cleaning up sim memory, sms count: " + lastPullSmsCount)
        deleteAllSms()
      }

      Thread.sleep(10000)
    }
  }

  def shutdown(){
    _stop = true
  }

  def reply(fromNumber:String, str:String){}
}

//trait GammuSmsProcessor {
//  def process(sms:Sms)
//}
//
//class ReconSmsProcessor extends GammuSmsProcessor {
//  def process(sms: Sms) {
//
//  }
//}
//

