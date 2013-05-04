package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */


trait SmsStatus {
  override def toString = getClass.getSimpleName
}


object SmsStatus {
  object Unread extends SmsStatus {
    override def toString = "Unread"
  }
  object Read extends SmsStatus {
    override def toString = "Read"
  }
}
