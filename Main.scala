package documentClustering

import scala.collection.mutable.ListBuffer

object Main {

  /** Main Function
    * @param args: Array(String)
    * @return NA
    */
  def main(args: Array[String]) {

    val nlpUtils = new NlpUtils()

    val kmeans = new KMeans()

    val links =
      List("http://www3.northern.edu/marmorsa/grphilosnotes.htm",
      "https://en.wikipedia.org/wiki/Crater_Lake",
      "https://en.wikipedia.org/wiki/Supervised_learning",
      "http://www3.northern.edu/marmorsa/greekdreamrevised.htm",
      "https://en.wikipedia.org/wiki/Machine_learning",
      "https://en.wikipedia.org/wiki/Lake_Tahoe",
      "https://en.wikipedia.org/wiki/Unsupervised_learning",
      "https://en.wikipedia.org/wiki/Glass_Beach_(Fort_Bragg,_California)",
      "http://www3.northern.edu/marmorsa/greekachievements2007.htm",
      "http://www3.northern.edu/marmorsa/grdramanotes.htm",
      "https://en.wikipedia.org/wiki/History_of_modern_Greece",
      "https://en.wikipedia.org/wiki/History_of_Greece",
      "https://en.wikipedia.org/wiki/Lake_Michigan"
    )

    var contents = new ListBuffer[Document]()

    println("Accumulating and cleaning provided links")
    for(link <- links){

      val (title, tokens) = nlpUtils.cleanHtmlToTokens(link)

      val featureVector = nlpUtils.hashingVectorizer(tokens, 5000)
      val content = Document(title,tokens,featureVector)
      contents += content
    }

    kmeans.fit(contents, 3)

  }
}
