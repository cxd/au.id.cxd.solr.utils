package au.id.cxd.solr.utils.filesystem

import au.id.cxd.solr.utils.PathResolver
import au.id.cxd.solr.utils.examples.TraversalResult
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers



class TestFileSystem extends AnyFlatSpec with Matchers {



  "Filesystem" should "traverse files" in {
    val path = PathResolver()
    println(path)
    val source = s"$path/../../src/test/data"
    val filter = "**/*.pdf"
    val files = Traversal(source, filter).search( fileResult => {
      val name = fileResult.file.path.toString
      println(s"Process File: $name")
      TraversalResult(name)
    })
    files.length should not be(0)
  }

}
