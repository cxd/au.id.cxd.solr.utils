package au.id.cxd.solr.utils.examples
import au.id.cxd.solr.utils.PathResolver
import au.id.cxd.solr.utils.config.{ReadSyntax, SolrConfig, SolrConfigRead}
import au.id.cxd.solr.utils.config.SolrConfigRead.solrRead
import au.id.cxd.solr.utils.model.FileResult
import au.id.cxd.solr.utils.pipeline.ResultPipeline.fileToTaggedResult
import au.id.cxd.solr.utils.pipeline.{ResultPipeline, ResultPipelineSyntax}
import au.id.cxd.solr.utils.solr.Connection
import better.files.File


object ExampleLoad {

  implicit val defaultConfig = "solr.conf"

  /**
   * test loading so as to be able to debug.
   * @param args
   */
  def main(args:Array[String]) = {

    val path = PathResolver()
    println(path)
    val config = s"$path/../../src/test/data/doesnotexist.conf"

    import SolrConfigRead._
    import ReadSyntax._

    val solr1 = SolrConfig("na", "na", 0, 0)

    val solrOpt = solr1.read(solrRead)(config, defaultConfig)

    val source = s"$path/../../src/test/data"
    val testfile = File(s"$source/scala/essential-scala.pdf")
    val testresult = FileResult(testfile)

    import ResultPipeline._
    import ResultPipelineSyntax._

    // run the composed pipeline without intermediate results.
    val step3 = fileToTaggedResult(testresult)

    // now we want to test writing the stream.
    val connection = Connection(solrOpt.get)

    val ignore = connection.checkExists(solrOpt.get.core, step3.uniqueId) match {
      case true => connection.delete(solrOpt.get.core, step3.uniqueId)
      case _ => ()
    }

    val results = connection.extract(solrOpt.get.core,
      step3.uniqueId,
      step3.name,
      step3.tags,
      new java.io.File(step3.filePath),
      "application/pdf"
    )

    val len = results.size()

    // example can be retrieved using a query such as:
    // http://numeric.local:8983/solr/gettingstarted/select?df=attr_content&fl=id%2C%20name%2Cattr_category%2Cattr_title%2Cattr_meta_author%2Cattr_created%2Cattr_date&hl.fl=attr_content&hl=on&q=*types*&rows=100


  }
}
