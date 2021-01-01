package au.id.cxd.solr.utils.filesystem

import better.files._

import java.io.File._
import java.io.{File => JFile}
import au.id.cxd.solr.utils.model.{FileResult, Result}

/**
 * The traversal takes a base path, a filter to set the pattern to traverse the path via and an action function which returns a FileResult
 * @param basePath
 * @param filter
 * The filter may specify a match pattern to glob files from the base path,
 * Globbing patterns are described here: https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
 * @param actionFn
 **/
case class Traversal(basePath:String, filter:String) {

  /**
   * search the base path and extract any set of files matching the supplied filter.
   *
   * @return
   */
  def search(actionFn:FileResult => Result):Iterator[Result] = {
    val sourceDir = new JFile(basePath).toScala
    val searchResult = sourceDir.glob(filter, includePath=true)
    searchResult.map { FileResult(_) }
      .map { actionFn }
  }

}
