package com.ansvia

import com.ansvia.commons.logging.Slf4jLogger
import com.redis.RedisClient

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
class GammuDaemon extends Thread with Gammu with Slf4jLogger {

  private lazy val backend = new GammuRedisStorage("localhost", 6379)
  private var _stop = false

  override def run() {

    while (!_stop){

      info("reading smses from device...")

      // dapatkan data dari sim card
      // lalu masukkan ke storage
      pull() foreach { sms =>
        if (sms.status != SmsStatus.Read) // hanya untuk sms yang belum pernah dibaca.
          backend.push(sms, Folder.Inbox)
      }


      Thread.sleep(5000)
    }
  }

  def shutdown(){
    _stop = true
  }
}

class GammuDaemonSender extends Thread with Gammu with Slf4jLogger {

  private lazy val backend = new GammuRedisStorage("localhost", 6379)
  private var _stop = false

  override def run() {

    while (!_stop){

      info("searching for to sent sms...")

      // get data dari storage lalu
      // kirimkan satu per satu
      backend.pop(Folder.Outbox) map { sms =>
        send(sms.toNumber, sms.message)
      }


      Thread.sleep(1000)
    }
  }

  def shutdown(){
    _stop = true
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
}

class GammuRedisStorage(host:String, port:Int) extends GammuStorageBackend with Slf4jLogger {

  lazy val rc = new RedisClient(host, port)

  def isExists(sms:Sms, folder:Folder):Boolean = {
    folder match {
      case Folder.Inbox =>
        rc.lrange('gammu_inbox, 0, -1).map { lst =>
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
    }
  }

}

