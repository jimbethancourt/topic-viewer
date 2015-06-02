![https://dl.dropboxusercontent.com/u/67114006/page/topicviewer.png](https://dl.dropboxusercontent.com/u/67114006/page/topicviewer.png)

The `TopicViewer` tool is a desktop application written in Java. Soon as the user starts the application, there are four activities: (i) `Set Workspace`, i.e. a folder where any output data will be stored; (ii) `Java Vocabulary Extraction`, to extract vocabulary from Java source code, (iii) `Semantic Clustering`, in order to generate semantic clusters and distribution map given a term-document matrix, and (iv) `Distribution Map Viewer`, to interact with the map structure, see classes, semantic topics and conceptual quality metrics.

![https://dl.dropboxusercontent.com/u/67114006/page/architecture.png](https://dl.dropboxusercontent.com/u/67114006/page/architecture.png)

Our tool reuses Information Retrieval functions from [VocabularyTools](https://sites.google.com/site/softwarevocabularies/home/towards-a-prediction-model-for-source-code-vocabulary), a set of different tools for extraction, filtering and LSI operations in Java, and developed by the Software Practices Lab of Federal University of Campina Grande (UFCG). The current `TopicViewer` implementation follows a MVC architectural pattern with three main modules, shown in the figure above:

  * Data: Representation of main data structures of the tool. Includes matrix storage and  operations in file, representation of a Distribution Map and utility functions to save these data structures and other necessary information in files.
  * Controller: Contains the main algorithms: the agglomerative hierarchical clustering, semantic topics extraction, as well as an interface for `VocabularyTools` operations.
  * View: A set of Swing developed windows providing an user interface for parameter input, as well as a graphic representation of Distribution Map.

![https://dl.dropboxusercontent.com/u/67114006/page/semanticclustering.png](https://dl.dropboxusercontent.com/u/67114006/page/semanticclustering.png)

The interface for Semantic Clustering is shown above. The user can choose (left) which clustering stop criteria will be used, as a fixed number of clusters or by a similarity threshold. Next, he can choose a set of term-document matrices to cluster (right) and monitor the execution progress on the bottom bar.