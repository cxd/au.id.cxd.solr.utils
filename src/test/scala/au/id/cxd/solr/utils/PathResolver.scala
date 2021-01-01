package au.id.cxd.solr.utils

import better.files._

import java.io.{File => JFile}
import java.nio.file.Path

object PathResolver {

  def apply():Path = {
    val clazz = PathResolver.getClass
    val path = new JFile(clazz.getProtectionDomain.getCodeSource.getLocation.toURI).toScala
    path.path.getParent
  }
}
