package au.id.cxd.solr.utils.examples

import java.io._
import au.id.cxd.solr.utils.filesystem.Traversal
import au.id.cxd.solr.utils.model.Result

case class TraversalResult(val filename:String) extends Result {

}

object ExampleTraversal {

  def writeOutput (results:Iterator[Result]) = {
    val fileout = new PrintWriter(new File("test_traversal.txt"))
    results.foreach {
      result => result match {
        case TraversalResult(filename) =>
          fileout.println(filename)
        case _ => ()
      }
    }
    fileout.flush()
    fileout.close()
  }

  def main(args:Array[String]):Unit = {

    val path = args.take(1).head
    val filter = args.take(2).last

    println(s"Path: $path Filter: $filter")

    val files = Traversal(path, filter).search( fileResult => {
      val name = fileResult.file.path.toString
      println(s"Process File: $name")
      TraversalResult(name)
    })
    writeOutput(files)
  }

}
