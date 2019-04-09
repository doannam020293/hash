package vn.cicdata.common.util

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import GlobalUtils.StringImprovements
import vn.cicdata.common.io.Config


object NormalizeUDF {

  def normalize(msisdn: String) = {
    val msisdnLong = msisdn.toLongCatch
    if (msisdn.toLongCatch == 0L){
      msisdn
    }
    else {
      msisdnLong.toString
    }
  }

  def getFun(): UserDefinedFunction = udf(normalize _)


}

