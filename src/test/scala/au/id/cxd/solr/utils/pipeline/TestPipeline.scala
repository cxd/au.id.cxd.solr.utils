package au.id.cxd.solr.utils.pipeline

import au.id.cxd.solr.utils.PathResolver
import au.id.cxd.solr.utils.model.FileResult
import better.files.File
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import au.id.cxd.solr.utils.pipeline._

class TestPipeline extends AnyFlatSpec with Matchers {

  "Pipeline" should "convert to unique id" in {
    val path = PathResolver()
    println(path)
    val source = s"$path/../../src/test/data"
    val testfile = File(s"$source/scala/essential-scala.pdf")
    val testresult = FileResult(testfile)

    import ResultPipeline._

    val step1 = fileToFilePath.run(testresult)
    val step2 = filePathToUniqueResult.run(step1)

    println(step2.uniqueId)

    step2.uniqueId should not be("")

    val step2b = filePathToUniqueResult.run(step1)
    step2b.uniqueId.equalsIgnoreCase(step2.uniqueId) should be(true)

  }

  "TaggedResult" should "tag with parent path name" in {
    val path = PathResolver()
    println(path)
    val source = s"$path/../../src/test/data"
    val testfile = File(s"$source/scala/essential-scala.pdf")
    val testresult = FileResult(testfile)

    import ResultPipeline._
    import ResultPipelineSyntax._

    // run the composed pipeline without intermediate results.
    val step3 = fileToTaggedResult(testresult)


    val tags = step3.tags.mkString(",")
    println(tags)

    step3.tags.length should not be(0)

    step3.tags.head should equal("scala")
  }

}
