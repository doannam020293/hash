package vn.cicdata.common.encrypt

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import vn.cicdata.common.io.Config
import vn.cicdata.common.util.GlobalUtils.StringImprovements


object HashUDF {

  val configKey = new Config("key.properties")
  val hashKey1 = configKey.getProperty("input.hash.key1").toLong
  val hashKey2 = configKey.getProperty("input.hash.key2").toLong
  def hash(msisdn: String) = {
    if (msisdn.toLongCatch == 0L){
      msisdn
    }
    else {
      SipHashInline.hash(hashKey1, hashKey2, msisdn.toLongCatch)

    }
  }

  def getFun(): UserDefinedFunction = udf(hash _)

  def main(args: Array[String]): Unit = {
    println(SipHashInline.hash(hashKey1, hashKey2, "01530987267940".toLongCatch))
    println(SipHashInline.hash(hashKey1, hashKey2, 1530987267940L))
    println("01530987267940".toLongCatch)
  }
}

