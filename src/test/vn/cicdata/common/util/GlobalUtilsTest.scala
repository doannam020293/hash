package vn.cicdata.common.util

import vn.cicdata.common.encrypt.SipHashInline
import vn.cicdata.common.util.GlobalUtils.StringImprovements

object GlobalUtilsTest  {
  def main(args: Array[String]): Unit = {
    val x = "84004333348507"
    println(SipHashInline.hash(448862796L, 13335914, x.toLongCatch))
    println(SipHashInline.hash(0L, 0L, "a".toLongCatch))
  }
}
