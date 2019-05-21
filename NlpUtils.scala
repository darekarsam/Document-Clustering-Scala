package documentClustering

import org.jsoup.Jsoup

import scala.collection.mutable.ListBuffer
import scala.io.Source

class NlpUtils {


  /** Function to convert HTML to raw Tokens seperated by " "
    * @param link: String
    * @return (head of Doc, tokens): (String, Array(String))
    */
  def cleanHtmlToTokens(link: String) = {

    //    get HTML from the URL
    val rawHtmlString = Source.fromURL(link).mkString
    val doc = Jsoup.parse(rawHtmlString) //Parse HTML to Document

    //    convert to lowercase, replace all punctuations by space and remove digits
    val strippedString = doc.body.text()
      .toLowerCase.replaceAll("""([\p{Punct}])""", " ")
      .replaceAll("\\d", "")
      .replaceAll("[^\\x00-\\x7F]", " ") // replace Non ASCII characters

    //    list of stopwords from https://gist.github.com/sebleier/554280
    //    adding empty string to the list
    val stopWords =
    List("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "should", "now",
      "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself",
      "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who",
      "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being",
      "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if",
      "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "didn",
      "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up",
      "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there",
      "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such",
      "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don",
      "")

    //    split by space and filter stop words and blanks also filter words less than length 2
    val rawTokens = strippedString
      .split(" ")
      .filter(word => !stopWords.contains(word) && word.length>2)

    //    Return head and tokens and rawString for the URL
    (doc.head.text(), rawTokens)


  }


  /** Function to create hash of word and get Index in the feature vector
    * @param word: String
    * @param vectorLength: Int
    * @return (Index, wordHash): (Int, Int)
    */
  def getIndexOfWordAndHash(word:String, vectorLength:Int)={

//    Reference: https://docs.oracle.com/javase/7/docs/api/java/lang/String.html#hashCode()
    val wordHash = word.hashCode

    var index = wordHash % vectorLength

//    if index is negative add the index with vectorLength to get positive index
    if(index < 0){
      index = index + vectorLength
    }

    (index, wordHash)
  }

  /** Function to Normalize the feature vector
    * @param vector: Vector(Int)
    * @return Normalized Vector: Vector(Double)
    */
  def normalizeVector(vector: Vector[Int])={
    val denominator = math.sqrt(vector.map(x => x*x).foldLeft(0)(_ + _))
    val normalizedVec = vector.map(x => x/denominator)
    normalizedVec
  }

  /** Function to create feature vector using Hashing
    * @param tokens: Array(String)
    * @param vectorLength: Int
    * @return featureVector: Vector(Double)
    */
  def hashingVectorizer(tokens:Array[String], vectorLength:Int) = {
    val docVectorList = List.fill(vectorLength)(0)
    var docVector = docVectorList.to[ListBuffer]

    for(token <- tokens){
      val (index, wordHash) =  getIndexOfWordAndHash(token, vectorLength)
      docVector(index) += 1
//      todo: implement code for collision using worHash
    }

    val normalizedVec = normalizeVector(docVector.toVector)
    normalizedVec
  }

}
