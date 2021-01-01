package au.id.cxd.solr.utils.filesystem

import scala.jdk.CollectionConverters._
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

object Md5 {

  /**
   * calculate md5 string for byte array
   * @param data
   * @return
   */
  def apply(data:Array[Byte]):String = {
    val md:MessageDigest = MessageDigest.getInstance("MD5")
    val digest = md.digest(data)
    val hash = DatatypeConverter.printHexBinary(digest).toUpperCase
    hash
  }
}
