package au.id.cxd.solr.utils.filesystem

import java.io.{File, FileInputStream}
import scala.collection.mutable.ArrayBuffer

object Binary {

  def apply(filePath:String):ArrayBuffer[Byte] = {
    val input:java.io.FileInputStream = new FileInputStream(filePath)
    val data = collection.mutable.ArrayBuffer[Byte]()
    do {
      data += input.read().toByte
    } while(input.available() > 0)
    data
  }

}
