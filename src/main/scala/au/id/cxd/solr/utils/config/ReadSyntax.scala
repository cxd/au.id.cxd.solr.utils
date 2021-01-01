package au.id.cxd.solr.utils.config

object ReadSyntax {
  implicit class Reader[A](value:A) {
    def read(implicit instance:Read[A]): (String, String) => Option[A] = instance.read(_)(_)
  }
}
