package com.ansvia

import com.ansvia.commons.logging.Slf4jLogger
import com.redis.RedisClient

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
class GammuDaemonFetcher extends Thread with Gammu with Slf4jLogger {

  protected lazy val backend = new GammuRedisStorage("localhost", 6379)
  private var _stop = false

  override def run() {

    var emptyCount = 0

    while (!_stop){

      info("reading smses from device...")

      // dapatkan data dari sim card
      // lalu masukkan ke storage
      val smses = pull()
      if (smses.length == 0)
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

class GammuDaemonSender extends Thread with GammuSmsWriter with Slf4jLogger {

  protected lazy val backend = new GammuRedisStorage("localhost", 6379)
  private var _stop = false

  override def run() {

    while (!_stop){

      info("searching for to sent sms...")

      // get data dari storage lalu
      // kirimkan satu per satu
      backend.pop(Folder.Outbox) map { sms =>
        debug("sending sms to " + sms.toNumber + ", fetched from outbox queue, len: " + sms.message.length)
        send(sms.toNumber, sms.message)
      }

      backend.pop(Folder.Draft) map { sms =>
        debug("sending sms to " + sms.toNumber + ", fetched from draft queue, len: " + sms.message.length)
        send(sms.toNumber, sms.message)
      }


      Thread.sleep(5000)
    }
  }

  def shutdown(){
    _stop = true
  }

  def sendSms(number:String, msg:String){
    val sms = Sms("", number, SmsStatus.Unread, "", "", msg)
    backend.push(sms, Folder.Outbox)
  }
}


trait GammuStorageBackend {
  def push(sms:Sms, folder:Folder)
  def pop(folder:Folder):Option[Sms]
}

trait Folder
object Folder {
  object Inbox extends Folder
  object Outbox extends Folder
  object Draft extends Folder
}

class GammuRedisStorage(host:String, port:Int) extends GammuStorageBackend with Slf4jLogger {

  lazy val rc = new RedisClient(host, port)

  def isExists(sms:Sms, folder:Folder):Boolean = {
    folder match {
      case Folder.Inbox =>
        rc.lrange('gammu_inbox, 0, -1).map { lst =>
          lst.contains(Some(sms.toJson))
        }.getOrElse(false)

      case Folder.Outbox =>
        rc.lrange('gammu_outbox, 0, -1).map { lst =>
          lst.contains(Some(sms.toJson))
        }.getOrElse(false)
      case Folder.Draft =>
        rc.lrange('gammu_draft, 0, -1).map { lst =>
          lst.contains(Some(sms.toJson))
        }.getOrElse(false)
    }
  }

  def push(sms: Sms, folder:Folder) {

    if (!isExists(sms, folder)){
      debug("pushing sms data to redis len: " + sms.toJson.length)
      val key = folder match {
        case Folder.Inbox => 'gammu_inbox
        case Folder.Outbox => 'gammu_outbox
        case Folder.Draft => 'gammu_draft
      }
      rc.rpush(key, sms.toJson)
    }

  }

  def pop(folder:Folder) = {
    folder match {
      case Folder.Inbox =>
        rc.lpop('gammu_inbox).flatMap(Sms.parseJson)
      case Folder.Outbox =>
        rc.lpop('gammu_outbox).flatMap(Sms.parseJson)
      case Folder.Draft =>
        rc.lpop('gammu_draft).flatMap(Sms.parseJson)
    }
  }

}

