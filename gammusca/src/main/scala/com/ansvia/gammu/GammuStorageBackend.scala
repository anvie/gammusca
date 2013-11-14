package com.ansvia.gammu

/**
 * Author: robin
 * Date: 5/5/13
 * Time: 8:41 AM
 *
 */
trait GammuStorageBackend {
    def push(sms:Sms, folder:Folder)
    def pop(folder:Folder):Option[Sms]
}
