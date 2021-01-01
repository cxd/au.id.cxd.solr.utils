package au.id.cxd.solr.utils.config

import com.typesafe.config.{Config, ConfigFactory}

import java.io.File
import scala.util.{Failure, Success, Try}

trait ConfigReader {

  /**
   * extract a value from configuration
   *
   * @param key
   * @param default
   * @param fn
   * @tparam T
   * @return
   */
  def configOrDefault[T](key: String)(fn: String => T)(implicit default: T): T = Try {
    fn(key)
  } match {
    case Success(value) => value
    case Failure(e) => default
  }


  /**
   * read from a configuration file path
   *
   * @param configFile
   * @return
   */
  def readFromFile(configFile: String): Option[Config] = Try {
    val file = new File(configFile)
    file.exists() match {
      case true => ConfigFactory.parseFile(new File(configFile))
      case _ => throw new Exception(s"$configFile is not a file")
    }
  }.toOption

  def readFromClassPath(configName: String): Option[Config] = Try {
    val cfg = ConfigFactory.load(getClass.getClassLoader,
      configName)
    cfg
  }.toOption

  /**
   * check if the supplied resource is in the class path.
   *
   * @param resource
   * @return
   */
  def checkClassPath(resource: String): Boolean = Try {
    val test = getClass.getClassLoader.getResource(resource)
    val f = new File(test.getFile)
    f.exists()
  } match {
    case Success(true) => true
    case _ => false
  }

  /**
   * read configuration from the class loader path as a resource.
   *
   * Note that two parameters are given.
   *
   * The first parameter is a file configuration path
   *
   * The second implicit parameter is the name of a resource configuration.
   *
   * if the file configuration path cannot be loaded this method will fallback to reading from the file resource configuration.
   *
   * @param configName
   * @return
   */
  def readConfig(configName: String)(implicit defaultConfig: String): Option[Config] = {
    val fileConfig = readFromFile(configName)
    fileConfig.isDefined match {
      case true => fileConfig
      case _ => checkClassPath(configName) match {
        case true => readFromClassPath(configName)
        case _ => readFromClassPath(defaultConfig)
      }
    }
  }

}
