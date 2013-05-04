package com.ansvia


import com.ansvia.commons.logging.Slf4jLogger


object Gammusca {

    def main(args:Array[String]){

      val daemon = new GammuDaemon()
      daemon.start()

      daemon.join()
    }

}

















