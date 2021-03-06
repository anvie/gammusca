package com.ansvia.gammu

import com.ansvia.commons.logging.Slf4jLogger


/**
 * Author: robin
 * Date: 5/5/13
 * Time: 8:41 AM
 *
 */
class GammuDaemonSender extends Thread with GammuSmsWriter with Slf4jLogger {

    var gammuBin = "/usr/bin/gammu"
    protected lazy val backend = new GammuRedisStorage("localhost", 6379)
    private var _stop = false

    override def run() {

        while (!_stop){

            debug("searching for to sent sms...")

            // get data dari storage lalu
            // kirimkan satu per satu
            backend.pop(Folder.Outbox) map { sms =>
                info("sending sms " + sms + ", fetched from outbox queue")
                safeSendSm(sms.toNumber, sms.message)
            }

            backend.pop(Folder.Draft) map { sms =>
                info("sending sms " + sms + ", fetched from draft queue")
                safeSendSm(sms.toNumber, sms.message)
            }


            Thread.sleep(5000)
        }
    }

    def shutdown(){
        _stop = true
    }

    def asyncSendSm(number:String, msg:String){
        val sms = Sms("", number, SmsStatus.Unread, "", "", msg)
        backend.push(sms, Folder.Outbox)
    }

    def safeSendSm(number:String, msg:String){
        try {
            send(number, msg)
        }catch{
            case e:Exception =>
                error(e.getMessage)
        }
    }
}
