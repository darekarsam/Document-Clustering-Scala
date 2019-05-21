package documentClustering

import scala.collection.mutable.ListBuffer

/**
  * Case class to structure document data
  */
case class Document(
  head: String,
  tokens:Array[String],
  featureVector:Vector[Double]
)