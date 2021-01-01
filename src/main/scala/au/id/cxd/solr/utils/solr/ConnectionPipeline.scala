package au.id.cxd.solr.utils.solr

import scala.jdk.CollectionConverters._
import au.id.cxd.solr.utils.config.SolrConfig
import au.id.cxd.solr.utils.model.{TaggedFileResult, UploadResult}
import au.id.cxd.solr.utils.pipeline.Pipeline

import java.io.File

case class ConnectionPipeline(val solrConfig:SolrConfig) extends Pipeline[TaggedFileResult, UploadResult] {

  val connection = Connection(solrConfig)
  val client = connection.getClient

  override def run(source: TaggedFileResult): UploadResult = {

    val results = connection.extract(solrConfig.core,
      source.uniqueId,
      source.name,
      source.tags,
      new File(source.filePath),
      solrConfig.contentType)

    val resultMap = results.asShallowMap().asScala

    UploadResult(sourceFile = source,
      results=resultMap)

  }
}
