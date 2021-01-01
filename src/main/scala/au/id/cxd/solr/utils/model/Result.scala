package au.id.cxd.solr.utils.model

import better.files.File

import java.nio.file.Path
import scala.collection.mutable

trait Result {}

case class FileResult(file:File) extends Result {}

case class FilePathResult(path:Path, filePath:String, name:String) extends Result {}

case class UniqueFileResult(uniqueId:String, path:Path, filePath:String, name:String) extends Result {}

case class TaggedFileResult(uniqueId:String, path:Path, filePath:String, name:String, tags:Seq[String]) extends Result {}

case class UploadResult(sourceFile:TaggedFileResult, results:mutable.Map[String,AnyRef]) extends Result {}
