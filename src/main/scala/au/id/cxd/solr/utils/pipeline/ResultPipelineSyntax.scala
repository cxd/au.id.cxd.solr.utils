package au.id.cxd.solr.utils.pipeline

/**
 * a syntax class as per the alvinj type class chapter.
 */
object ResultPipelineSyntax {

  implicit class ResultPipelineOps[A,B](value:A) {
    def run(implicit runInstance:Pipeline[A,B]):B = runInstance.run(value)
  }

}
