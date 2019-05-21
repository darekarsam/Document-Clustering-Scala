package documentClustering

import scala.collection.mutable.{ListBuffer, Map}
import math._
import scala.util.control.Breaks._



class KMeans {


  /** Function to get Euclidean Distance for two vectors
    * @param vector1 Vector of Double
    * @param vector2 Vector of Double
    * @return distance between the vectors: Double
    */
  def getEuclideanDist(vector1: Vector[Double], vector2: Vector[Double])={
    sqrt((vector1 zip vector2).map { case (x,y) => pow(y - x, 2) }.sum)
  }

  /** Function to calculate distance to each centroid and return the closest one
    * @param dataPoint Vector of Double
    * @param centroids List of Vectors(Centroids)
    * @return tuple(index of closest centroid, distance of closest centroid):(Int, Double)
    */
  def getClusterForDataPoint(dataPoint: Vector[Double], centroids: ListBuffer[Vector[Double]]) = {
//    calculate distance to all centroids
    val distTocentroid = centroids.map(centroid => getEuclideanDist(centroid, dataPoint))
//    return index of centroid with min distance
    (distTocentroid.zipWithIndex.min._2, distTocentroid.zipWithIndex.min._1)
  }

  /** Function to re-calculate Centroids
    * @param clusterBelongingMap Map(Vector(double), Int)
    * @param centroids List of Vectors(Centroids)
    * @return updated centroids : List(Vector)
    */
  def reCalculateCentroids(clusterBelongingMap: Map[Vector[Double], Int], centroids: ListBuffer[Vector[Double]]) = {
//    var newCentroids = ListBuffer[Vector[Double]]()

//    swap key value, i.e convert Map(data-> cluster) to Map(cluster->data)
    val clusterToDataPointMap = clusterBelongingMap.groupBy(_._2).mapValues(_.keys)
    for ((key, value) <- clusterToDataPointMap){

//      convert from Iterable(Vector[Double]) to List(Vector[Double]) i.e of type List(vectors[Double])
      val dataPointsList = value.toList

      if(dataPointsList.size > 1) {
        val updatedCentroid = dataPointsList.transpose.map(x => x.sum/x.size)
//        convert centroid from List[List[Double]] to ListBuffer[Vector[Double]] for returning centroid
        centroids(key) = updatedCentroid.toVector
      }

    }
    centroids
  }

  /** Function to check if tolerance criteria is satisfied
    * @param clusterDistMap Map(String, ListBuffer(double))
    * @param toleranceLevel Double
    * @return satisfied: Boolean
    */
  def toleranceCriteriaSatisfied(clusterDistMap: Map[String, ListBuffer[Double]], toleranceLevel: Double) = {
//    If tolerance criteria is satisfied i.e. diff < tolerance return true else false
    var satisfied = true
    breakable {
      for ((_, value) <- clusterDistMap) {
        //       if number of iterations are greater than 1
        if (value.size > 1) {
          //         get difference of last two values
          val diff = abs((value zip value.drop(1)).map { case (x, y) => y - x }.last)

          if (diff > toleranceLevel) {
            satisfied = false
            break
          }
        }

      }
    }
    satisfied
  }

  /** Function to fit KMeans Algorithm
    * @param data: ListBuffer(Document)
    * @param k number of Clusters: Int
    * @return NA
    */
  def fit(data: ListBuffer[Document], k: Int) = {
//    Steps:
//      1. Choose k Random docVecs as Centroids, set tolerance level tol and max iterations maxIter
//      2. assign dataPoint to cluster centroid whose distance is minimum among all centroids
//      2. re-calculate centroids by sum of all points in a cluster Ci/number of points in a cluster Ci
//      3. repeat step 2 and 3 until convergence
//      4. and return centroids

//    step 1
    var centroids = data.take(k).map(doc => doc.featureVector)

    val maxIter = 100
    val toleranceLevel = 0.0003
//    map for maintaining distance to nearest cluster per data point for calculating tolerance
    val clusterDistMap = Map[String, ListBuffer[Double]]()
//    Map for storing which datapoint belongs to which cluster
    val clusterBelongingMap = Map[Vector[Double], Int]()
    val docVecMap = Map[String, Vector[Double]]()
    breakable {
      for (iteration <- 1 to maxIter) {
        println("")
        println("Iteration # " + iteration)
        println("Centroids:")
        for(centroid <- centroids) {
          println(centroid)
        }

        for (dataPoint <- data) {
          val (clusterIndex, clusterDist) = getClusterForDataPoint(dataPoint.featureVector, centroids)

          //         Assign Current cluster Index
          clusterBelongingMap(dataPoint.featureVector) = clusterIndex
          println("Document : \"" + dataPoint.head + "\" assigned to Cluster: " + clusterIndex)
          //         append current distance between the selected cluster and datapoint
          if (clusterDistMap.isDefinedAt(dataPoint.head)) {
            clusterDistMap(dataPoint.head) += clusterDist
          }
          else {
            clusterDistMap(dataPoint.head) = ListBuffer(clusterDist)
          }


        }

        println("Re-Calculating Centroids")
        centroids = reCalculateCentroids(clusterBelongingMap, centroids)

        if (toleranceCriteriaSatisfied(clusterDistMap, toleranceLevel) && iteration>1) {
          println("tolerance criteria met...\n stopping train method ...")
          break
        }
      }
    }

  }

}
