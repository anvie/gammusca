package com.ansvia

/**
 * Author: robin
 * Date: 5/5/13
 * Time: 8:42 AM
 *
 */

trait Folder

object Folder {
  object Inbox extends Folder
  object Outbox extends Folder
  object Draft extends Folder
}
