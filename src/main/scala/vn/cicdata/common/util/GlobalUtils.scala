package vn.cicdata.common.util

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext
import org.apache.spark.sql.functions.udf
import vn.cicdata.common.encrypt.SipHashInline

import scala.io.Source
import scala.util.matching.Regex

object GlobalUtils {


  implicit class StringImprovements(s: String) {
    def toDoubleCatch = { try { s.toDouble; } catch { case e: Exception => { 0.0 } } }
    def toLongCatch = { try {
      val newString = s.normalize
//      if (newString.size <= 11){
        newString.toLong;
//      } else {
//        0L
//      }
    } catch { case e: Exception => { 0L } } }


    def isAllDigits = s forall Character.isDigit


    def normalize : String =  {

      if (s ==""){
        s
      } else {

        var newString : String = ""
        newString= s.replaceFirst("^\\+","")
        newString= newString.replaceFirst("^00","")


        if (newString.length < 10 && newString.startsWith("84")){
          newString = "84"+ newString
        } else  if (!newString.startsWith("84")){
          if (newString.length <= 10 && (!newString.startsWith("0"))){
            newString = "84" + newString
          } else  {
            if (newString.startsWith("0")) {
              newString = "84" +  newString.substring(1)
            }
          }
        }
        if (newString.length < 5 ){
          newString
        } else {
          newString = newString.replaceFirst("^84120", "8470")
          newString = newString.replaceFirst("^84121", "8479")
          newString = newString.replaceFirst("^84122", "8477")
          newString = newString.replaceFirst("^84126", "8476")
          newString = newString.replaceFirst("^84128", "8478")
          newString = newString.replaceFirst("^84123", "8483")
          newString = newString.replaceFirst("^84124", "8484")
          newString = newString.replaceFirst("^84125", "8485")
          newString = newString.replaceFirst("^84127", "8481")
          newString = newString.replaceFirst("^84129", "8482")
          newString = newString.replaceFirst("^84162", "8432")
          newString = newString.replaceFirst("^84163", "8433")
          newString = newString.replaceFirst("^84164", "8434")
          newString = newString.replaceFirst("^84165", "8435")
          newString = newString.replaceFirst("^84166", "8436")
          newString = newString.replaceFirst("^84167", "8437")
          newString = newString.replaceFirst("^84168", "8438")
          newString = newString.replaceFirst("^84169", "8439")
          newString = newString.replaceFirst("^84186", "8456")
          newString = newString.replaceFirst("^84188", "8458")
          newString = newString.replaceFirst("^84199", "8459")


          // convert case like 840043763636850 => 84376363850
          if (newString.size == 14 | newString.take(4) == "8400") {
            newString = "84" + newString.substring(5)
          }

          newString
        }


      }




    }


    def isMobileNumber: Boolean = {
      if (s.length == 0) false
      else {
        var input: String = ""
        if (s.startsWith("0")) input = s.substring(1)
        else if (s.startsWith("84")) input = s.substring(2)
        else input = s

        val hasValidLength = (input.length == 9) || (input.length == 10)

        val isAllDigits = input.isAllDigits

        val mobilePattern: Regex =
          """^(96|97|98|162|163|164|165|166|167|168|169|91|94|123|124|125|127|129|88|90|93|120|121|122|126|128|89|92|188|186|95|993|994|995|996|99|199).*""".r
        val isMobilePattern = mobilePattern.findFirstIn(input) match {
//        val isMobilePattern = mobilePattern.find(input) match {
          case Some(_) => true
          case None => false
        }

        hasValidLength && isAllDigits && isMobilePattern
      }
    }
  }

  def deleteIfExisted(path: String, sc: SparkContext): Unit = {
    val fs = FileSystem.get(sc.hadoopConfiguration)
    if (fs.exists(new Path(path))) fs.delete(new Path(path), true)
  }

  def testDirExist(path: String, hadoopfs: FileSystem ): Boolean = {
    val p = new Path(path)
    hadoopfs.exists(p) && hadoopfs.getFileStatus(p).isDirectory
  }

  def getConfig(filePath: String)= {
    Source.fromFile(filePath).getLines().filter(line => line.contains("=")).map{ line =>
      println(line)
      val tokens = line.split("=")
      ( tokens(0) -> tokens(1))
    }.toMap
  }



}
