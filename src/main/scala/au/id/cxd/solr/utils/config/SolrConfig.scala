package au.id.cxd.solr.utils.config

case class SolrConfig(val host:String, val core:String, val connectTimeout:Int, val socketTimeout:Int,
                      val filter:String,
                      val contentType:String) {

}
