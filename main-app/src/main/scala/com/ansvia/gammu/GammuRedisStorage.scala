package com.ansvia.gammu

import com.ansvia.commons.logging.Slf4jLogger
import com.redis.RedisClient

/**
 * Author: robin
 * Date: 5/5/13
 * Time: 8:42 AM
 *
 */
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
