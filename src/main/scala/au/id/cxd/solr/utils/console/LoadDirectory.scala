package au.id.cxd.solr.utils.console
import au.id.cxd.solr.utils.config.SolrConfigRead.solrRead
import au.id.cxd.solr.utils.config.{ReadSyntax, SolrConfig, SolrConfigRead}
import au.id.cxd.solr.utils.filesystem.Traversal
import au.id.cxd.solr.utils.model.{Result, TaggedFileResult}
import au.id.cxd.solr.utils.pipeline.ResultPipeline
import au.id.cxd.solr.utils.solr.Connection
import scopt.OParser

object LoadDirectory {

  implicit val defaultConfig:String = "solr.conf"

  /**
   * load the configuration from file
   * @param configFile
   * @return
   */
  def loadConfig(configFile:String):Option[SolrConfig] = {
    import SolrConfigRead._
    import ReadSyntax._

    val solr1 = SolrConfig("na", "na", 0, 0, "", "")

    val solrOpt = solr1.read(solrRead)(configFile, defaultConfig)
    solrOpt
  }

  /**
   * traverse the input directory using the supplied filter.
   * @param sourceDir
   * @param filter
   * @return
   */
  def traverseDirectories(sourceDir:String, filter:String): Iterator[Result] = {
    import ResultPipeline._
    val files = Traversal(sourceDir, filter).search( fileResult => {
      val name = fileResult.file.path.toString
      println(s"Process File: $name")
      fileToTaggedResult(fileResult)
    })
    files
  }

  /**
   * upload the results.
   * @param solrConfig
   * @param results
   */
  def uploadResults(solrConfig:SolrConfig, results:Iterator[Result]) = {
    val connection = Connection(solrConfig)

    results.foreach {
      result =>
        result match {
          case TaggedFileResult(uniqueId, path, filePath, name, tags) => {
            // remove if exists.
            if (connection.checkExists(solrConfig.core, uniqueId))
              connection.delete(solrConfig.core, uniqueId)

            val results = connection.extract(solrConfig.core,
              uniqueId,
              name,
              tags,
              new java.io.File(filePath),
              solrConfig.contentType
            )
            ()
          }
          case _ => ()
        }

    }
  }

  /**
   * entry point to traverse directory structure
   * read options and validate input
   * @param args
   */
  def main(args: Array[String]) = {
    val builder = OParser.builder[CommandOptions]
    val parser = {
      import builder._
      OParser.sequence(
        programName("LoadDirToSolr"),
        head("LoadDirectory", "1.0"),
        opt[String]('s', "sourcePath")
          .action((value, options) => options.copy(sourcePath = value))
          .text("sourcePath is the source directory to traverse to extract filtered files to upload to solr for the given configuration."),
        opt[String]('c', "configFile")
          .action((value, options) => options.copy(configFile=value))
          .text("configFile is the path to the configuration file that defines the solr server parameters (see README.md)")
      )
    }
    OParser.parse(parser, args, CommandOptions()) match {
      case Some(options) =>
        val source = options.sourcePath
        val configFile = options.configFile
        println(s"Starting with sourcePath: $source configFile: $configFile")
        val conf = loadConfig(configFile)
        if (conf.isEmpty)
          println(s"Failed to load configuration from $configFile")
        conf.foreach {
          solrConf =>
            val results = traverseDirectories(source, solrConf.filter)
            uploadResults(solrConf, results)
        }
        ()
      case _ =>
        ()
    }

  }

}
