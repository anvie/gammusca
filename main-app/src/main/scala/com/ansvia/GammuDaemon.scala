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

      pull() foreach { sms =>
        backend.push(sms, Folder.Inbox)
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

class GammuRedisStorage(host:String, port:Int) extends GammuStorageBackend {

  lazy val rc = new RedisClient(host, port)

  def push(sms: Sms, folder:Folder) {
    val key = folder match {
      case Folder.Inbox => "gammu_inbox"
      case Folder.Outbox => "gammu_outbox"
    }
    rc.rpush(key, sms.toString)
  }

  def pop(folder:Folder) = {
    folder match {
      case Folder.Inbox =>
        rc.lpop("gammu_inbox").flatMap(Sms.parseJson)
    }
  }

}

