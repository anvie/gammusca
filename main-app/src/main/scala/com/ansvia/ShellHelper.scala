package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:37 PM
 *
 */
trait ShellHelper {


  protected def debug(str:String)

  def exec(cmds:String*):String = {
    debug("executing command: " + cmds.reduceLeft(_ + " " + _))
    ShellHelper.safeExec(cmds: _*)
  }

}

object ShellHelper {
  import scala.sys.process._

  def safeExec(cmds:String*):String = {
    synchronized {
      val rv = cmds !!

      Thread.sleep(2000)

      rv
    }
  }
}
