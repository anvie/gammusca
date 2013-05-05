package com.ansvia


//import com.ansvia.commons.logging.Slf4jLogger
//import ch.qos.logback.core.util.StatusPrinter
////import ch.qos.logback.classic.LoggerContext
////import ch.qos.logback.classic.joran.JoranConfigurator
//import ch.qos.logback.core.joran.spi.JoranException
import org.slf4j.LoggerFactory


object Gammusca {

    def main(args:Array[String]){

      println("Preparing...")

      val log = LoggerFactory.getLogger(getClass)
      log.info("Log setup...")
      Thread.sleep(5000)
//      val context:LoggerContext = LoggerFactory.getILoggerFactory
//
//      try {
//        val configurator = new JoranConfigurator()
//        configurator.setContext(context)
//        // Call context.reset() to clear any previous configuration, e.g. default
//        // configuration. For multi-step configuration, omit calling context.reset().
//        context.reset()
//        configurator.doConfigure(args[0])
//      } catch {
//        case e:JoranException =>
//        // StatusPrinter will handle this
//      }
//      StatusPrinter.printInCaseOfErrorsOrWarnings(context)

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

















