import au.id.cxd.solr.utils.PathResolver
import au.id.cxd.solr.utils.config.{ReadSyntax, SolrConfig, SolrConfigRead}
import au.id.cxd.solr.utils.config.SolrConfigRead.solrRead
import au.id.cxd.solr.utils.model.FileResult
import au.id.cxd.solr.utils.pipeline.ResultPipeline.fileToTaggedResult
import au.id.cxd.solr.utils.pipeline.{ResultPipeline, ResultPipelineSyntax}
import au.id.cxd.solr.utils.solr.Connection
import better.files.File

implicit val defaultConfig = "solr.conf"

val path = PathResolver()
println(path)
val source = s"$path/../../src/test/data/doesnotexist.conf"

import SolrConfigRead._
import ReadSyntax._

val solr1 = SolrConfig("na", "na", 0, 0)

val solrOpt = solr1.read(solrRead)(source, defaultConfig)

val source = s"$path/../../src/test/data"
val testfile = File(s"$source/scala/essential-scala.pdf")
val testresult = FileResult(testfile)

import ResultPipeline._
import ResultPipelineSyntax._

// run the composed pipeline without intermediate results.
val step3 = fileToTaggedResult(testresult)

// now we want to test writing the stream.
val connection = Connection(solrOpt.get)

val results = connection.extract(solrOpt.get.core,
  step3.uniqueId,
  step3.name,
  step3.tags,
  new java.io.File(step3.filePath),
  "application/pdf"
)

results