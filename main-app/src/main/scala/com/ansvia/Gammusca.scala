package com.ansvia


import com.ansvia.commons.logging.Slf4jLogger


object Gammusca {

    def main(args:Array[String]){

      println("Preparing...")
      Thread.sleep(20000)

      val daemonSender = new GammuDaemonSender()
      val daemonPuller = new GammuDaemonFetcher(){
        override def reply(fromNumber: String, str: String) {
          daemonSender.sendSms(fromNumber, str)
        }
      }


      daemonPuller.start()
      daemonSender.start()

      daemonPuller.join()
      daemonSender.join()

    }

}

















