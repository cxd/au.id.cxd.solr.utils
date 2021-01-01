package au.id.cxd.solr.utils.config

trait Read[A] extends ConfigReader {

  def read(configName:String)(implicit defaultConfig:String):Option[A]
}
