package au.id.cxd.solr.utils.solr

import au.id.cxd.solr.utils.config.SolrConfig
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.client.solrj.request.{AbstractUpdateRequest, ContentStreamUpdateRequest}
import org.apache.solr.client.solrj.response.UpdateResponse
import org.apache.solr.common.util.NamedList
import org.apache.solr.common.{SolrDocument, SolrInputDocument}

import java.io.File

case class Connection(val solrConfig:SolrConfig) {

  /**
   * get the client
   * @return
   */
  def getClient(): HttpSolrClient = new HttpSolrClient.Builder(solrConfig.host)
    .withConnectionTimeout(solrConfig.connectTimeout)
    .withSocketTimeout(solrConfig.socketTimeout)
    .build()

  /**
   * retrieve a document for the supplied core by its id
   * @param core
   * @param id
   * @return
   */
  def getDocument(core:String, id:String): SolrDocument =
    getClient.getById(core, id)

  /**
   * check that the given document exists
   * @param core
   * @param id
   * @return
   */
  def checkExists(core:String, id:String):Boolean =
    getDocument(core, id) != null

  /**
   * add a simple text content to the repository
   * @param core
   * @param id
   * @param name
   * @param categories
   */
  def add(core:String, id:String, name:String, categories:Seq[String], content:String): UpdateResponse = {
    val client = getClient
    val inputDoc = new SolrInputDocument()
    inputDoc.addField("id", id)
    inputDoc.addField("name", name)
    inputDoc.addField("categories", categories.mkString(","))
    inputDoc.addField("attr_content", content)
    val response = client.add(core, inputDoc)
    response
  }

  /**
   * use the extract request on the server to extract content from pdf, word or other supported file types.
   * Based on example from:
   * https://cwiki.apache.org/confluence/display/solr/ContentStreamUpdateRequestExample
   * @param core
   * @param id
   * @param name
   * @param categories
   * @param file
   * @return
   */
  def extract(core:String, id:String, name:String, categories:Seq[String], file:File, contentType:String): NamedList[AnyRef] = {
    val client = getClient
    val update = new ContentStreamUpdateRequest(s"/$core/update/extract")
    update.addFile(file, contentType)
    update.setParam("id", id)
    update.setParam("literal.id", id)
    update.setParam("name", name)
    update.setParam("literal.name", name)
    update.setParam("categories", categories.mkString(","))
    update.setParam("literal.categories", categories.mkString(","))
    update.setParam("uprefix", "attr_")
    update.setParam("fmap.content", "attr_content")
    update.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true)
    val response = client.request(update)
    response
  }

  /**
   * delete a document by its id.
   * @param core
   * @param id
   * @return
   */
  def delete(core:String, id:String): UpdateResponse = {
    val client = getClient
    client.deleteById(core, id)
  }

}
