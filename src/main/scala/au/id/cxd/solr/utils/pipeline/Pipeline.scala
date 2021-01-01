package au.id.cxd.solr.utils.pipeline

trait Pipeline[A,B] {

  def run(source:A):B

}
