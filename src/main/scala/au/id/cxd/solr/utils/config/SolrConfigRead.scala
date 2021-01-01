package au.id.cxd.solr.utils.config

object SolrConfigRead {

  implicit val solrRead = new Read[SolrConfig] {
    override def read(configName: String)(implicit defaultConfig: String): Option[SolrConfig] = {
      readConfig(configName).map {
        conf =>
          val host = conf.getString("solr.host")
          val core = conf.getString("solr.core")
          val timeout = conf.getInt("solr.connectTimeout")
          val socketTimeout = conf.getInt("solr.socketTimeout")
          val filter = conf.getString("solr.filter")
          val contentType = conf.getString("solr.contentType")
          SolrConfig(host=host,
            core=core,
            connectTimeout=timeout,
            socketTimeout=socketTimeout,
            filter=filter,
            contentType=contentType)
      }
    }
  }

}
