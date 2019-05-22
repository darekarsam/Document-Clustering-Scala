# Document-Clustering-Scala

## Used following web pages to get the HTML doc   

http://www3.northern.edu/marmorsa/grphilosnotes.htm
https://en.wikipedia.org/wiki/Crater_Lake
https://en.wikipedia.org/wiki/Supervised_learning
http://www3.northern.edu/marmorsa/greekdreamrevised.htm
https://en.wikipedia.org/wiki/Machine_learning
https://en.wikipedia.org/wiki/Lake_Tahoe
https://en.wikipedia.org/wiki/Unsupervised_learning
https://en.wikipedia.org/wiki/Glass_Beach_(Fort_Bragg,_California)
http://www3.northern.edu/marmorsa/greekachievements2007.htm
http://www3.northern.edu/marmorsa/grdramanotes.htm
https://en.wikipedia.org/wiki/History_of_modern_Greece
https://en.wikipedia.org/wiki/History_of_Greece
https://en.wikipedia.org/wiki/Lake_Michigan
    
## Web Page to Feature Vector(using Hashing trick and Normalization):
I have used the jsoup library to clean the web pages and got the text from the body tags.    
Removed all the punctuations and non ASCII characters(Still mode could have been done).   
Tokenized the text by seperating with space, Used only 1-grams however a complex and better solution could have been made using 2grams and 3grams.     
Removed stop words by using a predefined list of stopwords.    
I fixed the length of feature vector to 5000, for every token I got the hash and took the mod value divided by vector length and determined the index in feature vector.    
Complex collision handling code could have been written to handle collisions in a better way.    
Normalized the Feature vector by dividing by the magnitude and getting the unit vector.    

## KMeans clustering steps     
Steps:    
1. Choose k(Number of Clusters=3) Random vectors as Centroids, set tolerance level tol=0.0003(in this case) and max iterations maxIter=100     
2. Assign each data point to cluster whose distance is minimum among all centroids of the 3 clusters      
2. re-calculate centroids by sum of all points in a cluster Ci/number of points in a cluster Ci      
3. repeat step 2 and 3 until convergence      


## Results     
![Results](https://github.com/darekarsam/Document-Clustering-Scala/blob/master/KMeans%20Clustering%20Results.png)

Here we see that cluster 0 belongs to greek history documents and wiki document on Greece.    
cluster 1 belongs to Wiki pages of lakes.    
cluster 2 belongs to wiki documents related to machine learning.
