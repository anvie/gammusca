package com.ansvia

/**
 * Author: robin
 * Date: 5/4/13
 * Time: 10:37 PM
 *
 */
trait ShellHelper {
  import scala.sys.process._

  def exec(cmds:String*):String = {
    cmds !!
  }

}
