package au.id.cxd.solr.utils.config

import au.id.cxd.solr.utils.PathResolver
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TestConfig extends AnyFlatSpec with Matchers {

  "Config syntax" should "read config from file" in {

    implicit val defaultConfig = "solr.conf"

    import SolrConfigRead._
    import ReadSyntax._

    val path = PathResolver()
    println(path)
    val source = s"$path/../../src/test/data/solrdata.conf"
    val solrOpt = solrRead.read(source)
    solrOpt should not be(None)

    println(solrOpt.get.host)
    println(solrOpt.get.core)

    solrOpt.get.host should equal("http://numeric.local:8983/")

  }


  "Config syntax" should "read config from resource" in {

    implicit val defaultConfig = "solr.conf"

    import SolrConfigRead._
    import ReadSyntax._

    val path = PathResolver()
    println(path)
    val source = s"$path/../../src/test/data/doesnotexist.conf"
    val solrOpt = solrRead.read(source)
    solrOpt should not be(None)

    println(solrOpt.get.host)
    println(solrOpt.get.core)

    solrOpt.get.host should equal("http://numeric.local:8983/")

  }



  "Config syntax" should "pimp read behaviour" in {

    implicit val defaultConfig = "solr.conf"

    import SolrConfigRead._
    import ReadSyntax._

    val path = PathResolver()
    println(path)
    val source = s"$path/../../src/test/data/doesnotexist.conf"

    val solr1 = SolrConfig("na", "na", 0, 0, "", "")

    val solrOpt = solr1.read(solrRead)(source, defaultConfig)

    solrOpt should not be(None)

    println(solrOpt.get.host)
    println(solrOpt.get.core)

    solrOpt.get.host should equal("http://numeric.local:8983/solr")

  }
}
