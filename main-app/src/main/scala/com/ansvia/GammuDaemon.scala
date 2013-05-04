package com.ansvia

import com.ansvia.commons.logging.Slf4jLogger

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:38 PM
 *
 */
class GammuDaemon extends Thread with Gammu with Slf4jLogger {

  private var _stop = false

  override def run() {

    while (!_stop){

      info("reading smses from device...")

      val data = pullRaw()


      Thread.sleep(1000)
    }
  }

  def shutdown(){
    _stop = true
  }
}
