package au.id.cxd.solr.utils.pipeline

import au.id.cxd.solr.utils.filesystem.{Binary, Md5}
import au.id.cxd.solr.utils.model.{FilePathResult, FileResult, TaggedFileResult, UniqueFileResult}

import java.io.{ByteArrayInputStream, File, FileInputStream, FileReader}
import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.collection.mutable._

object ResultPipeline {

  /**
   * convert from a file result into a file path result
   */
  implicit val fileToFilePath = new Pipeline[FileResult, FilePathResult] {
    override def run(source: FileResult): FilePathResult =
      FilePathResult(path=source.file.path,
        filePath=source.file.path.toString,
        name=source.file.path.getFileName.toString)
  }

  /**
   * convert from a file path result into a unique id file result.
   * unique ids are the md5 hash of the file contents.
   */
  implicit val filePathToUniqueResult = new Pipeline[FilePathResult, UniqueFileResult] {
    override def run(source: FilePathResult): UniqueFileResult = {
      val data = Binary(source.filePath)
      val uniqueId = Md5(data.toArray)
      UniqueFileResult(uniqueId=uniqueId,
        path=source.path,
        filePath=source.filePath,
        name=source.name)
    }
  }

  /**
   * assign the immediate parent directory path name as the tag file the resulting file.
   */
  implicit val uniqueResultToTaggedResult = new Pipeline[UniqueFileResult, TaggedFileResult] {
    override def run(source: UniqueFileResult): TaggedFileResult = {
      val path = source.path
      val parent = path.getParent.getFileName
      TaggedFileResult(uniqueId=source.uniqueId,
        path=source.path,
        filePath=source.filePath,
        name=source.name,
        tags=Array[String](parent.toString))
    }
  }

  /**
   * chain togethor the pipeline.
   */
  implicit val fileToTaggedResult = {
    (uniqueResultToTaggedResult.run(_)).compose(
      (filePathToUniqueResult.run(_)) compose (fileToFilePath.run(_)))
  }

}
