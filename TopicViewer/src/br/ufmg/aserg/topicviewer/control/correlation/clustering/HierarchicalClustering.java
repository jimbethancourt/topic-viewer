package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;

public class HierarchicalClustering {

	private int numDocuments;
	private int minClusters;
	private double threshold;

	private DisjointTree clustersTree;

	private int[][] clusters;
	private Set<Integer> rowsForUpdating = new HashSet<>();

	public HierarchicalClustering(CorrelationMatrix correlationMatrix, boolean useThreshold, boolean useBestThreshold, int numClusters, double threshold) throws IOException {
		this.numDocuments = correlationMatrix.getNumEntities();
		this.minClusters = numClusters;
		this.threshold = threshold;

		this.initDisjointTree();
		DoubleMatrix2D correlationMatrix2D = correlationMatrix.getCorrelationMatrix();

		if (!useThreshold) this.threshold = 0D;
		else if (useBestThreshold) {
			this.threshold = getBestThreshold(correlationMatrix2D.copy());
			this.initDisjointTree();
		}

		rowsForUpdating = new HashSet<>();
		for (int i = 0; i < correlationMatrix2D.rows()-1; i++) {
			rowsForUpdating.add(i);
		}

		initClustering(correlationMatrix2D.copy());
	}

	public int[][] getClusters() {
		return this.clusters;
	}

	private void initDisjointTree() {
		this.clustersTree = new DisjointTree();
		for (int i = 0; i < this.numDocuments; i++)
			this.clustersTree.makeSet(new Vertex(i));
	}

	private void initClustering(DoubleMatrix2D correlationMatrix) {
		int numClusters = this.numDocuments;


		int[] leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, numDocuments, true);
		while (this.minClusters < numClusters && leastDissimilarPair != null) {
			Vertex set1 = this.clustersTree.findSet(leastDissimilarPair[0]);
			Vertex set2 = this.clustersTree.findSet(leastDissimilarPair[1]);

			if (!set1.equals(set2)) {
				this.clustersTree.union(set1, set2);
				updateCorrelationMatrix(set1.index, correlationMatrix);
				numClusters--;
			}

			leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, numDocuments, false);
		}

		this.generateClusters();
	}

	private void generateClusters() {
		Map<Integer, List<Integer>> mapping = new HashMap<Integer, List<Integer>>();

		for (int i = 0; i < this.numDocuments; i++) {
			Vertex set = this.clustersTree.findSet(i);
			List<Integer> treeList = (mapping.containsKey(set.index) ? mapping.get(set.index) : new LinkedList<Integer>());
			treeList.add(i); mapping.put(set.index, treeList);
		}

		this.clusters = new int[mapping.keySet().size()][0];
		int i = 0;
		for (Integer set : mapping.keySet()) {
			List<Integer> tree = mapping.get(set);
			this.clusters[i] = new int[tree.size()];

			int j = 0;
			for (Integer v : tree) {
				this.clusters[i][j] = v; j++;
			}
			i++;
		}
	}

	private int[] getLeastDissimilarPair(DoubleMatrix2D correlationMatrix, int numDocuments, boolean force) {
		double bestSimilarity = Double.NEGATIVE_INFINITY;
		int[] leastDissimilarPair = {0,0};

		for (int i = 0; i < numDocuments; i++)
			for (int j = 0; j < numDocuments; j++) {
				if (i < j) {
					double similarity = correlationMatrix.get(i, j);
					if (similarity != Double.NEGATIVE_INFINITY && similarity > bestSimilarity) {
						bestSimilarity = similarity;
						leastDissimilarPair = new int[]{i, j};
					}
				}
			}

		return (bestSimilarity < this.threshold) ? (force ? leastDissimilarPair : null) : leastDissimilarPair;
	}

	// implementing average linkage
	private void updateCorrelationMatrix(int unionSet, DoubleMatrix2D correlationMatrix) {
		int union = this.clustersTree.findSet(unionSet).index;
		int localNumDocuments = this.numDocuments;

		//only using a CHM for the concurrent write properties
		ConcurrentHashMap<Pair, Integer> calculatedClusters = new ConcurrentHashMap<>();
		rowsForUpdating.parallelStream().forEach(i -> {
			for (int j = i + 1; j < correlationMatrix.rows(); j++) {
				int set1Index = this.clustersTree.findSet(i).index;
				int set2Index = this.clustersTree.findSet(j).index;

				Pair newPair = (set1Index < set2Index) ?
						new Pair(set1Index, set2Index) :
						new Pair(set2Index, set1Index);

				if (set1Index == union || set2Index == union) {
					//we only care about the unique Pair objects
					calculatedClusters.put(newPair, 0);
				}
			}
		});

		calculatedClusters.keySet().parallelStream().forEach(pair -> {
			double newValue;
			int set1Index = pair.first;
			int set2Index = pair.second;
			Set<Integer> set1 = getClusterSet(set1Index, localNumDocuments);
			Set<Integer> set2 = getClusterSet(set2Index, localNumDocuments);

			if (set1Index == set2Index)
				newValue = Double.NEGATIVE_INFINITY;
			else {
				double distance = 0D;
				for (int doc1 : set1)
					for (int doc2 : set2)
						distance += correlationMatrix.get(doc1, doc2);

				newValue = distance / (set1.size() * set2.size());
			}

			for (int doc1 : set1)
				for (int doc2 : set2) {
					correlationMatrix.set(doc1, doc2, newValue);
					correlationMatrix.set(doc2, doc1, newValue);
				}
		});
	}

	private Set<Integer> getClusterSet(int rootIndex, int numDocuments) {
		Set<Integer> clusterSet = new LinkedHashSet<>();
		for (int i = 0; i < numDocuments; i++)
			if (this.clustersTree.findSet(i).index == rootIndex)
				clusterSet.add(i);
		return clusterSet;
	}

	public double getBestThreshold(DoubleMatrix2D correlationMatrix) throws IOException {
		final double[] thresholdSet = {.55, .60, .65, .70, .75};
		double[] clusteringQuality = new double[thresholdSet.length];
		int thresholdIndex = thresholdSet.length-1;

		DoubleMatrix2D originalCorrelationMatrix = correlationMatrix.copy();

		int[] leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, numDocuments, true);
		while (leastDissimilarPair != null && thresholdIndex >= 0) {
			this.threshold = thresholdSet[thresholdIndex];

			Vertex set1 = this.clustersTree.findSet(leastDissimilarPair[0]);
			Vertex set2 = this.clustersTree.findSet(leastDissimilarPair[1]);

			if (!set1.equals(set2)) {
				this.clustersTree.union(set1, set2);
				updateCorrelationMatrix(set1.index, correlationMatrix);
			}

			leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, numDocuments, false);

			if (leastDissimilarPair == null && thresholdIndex >= 0) {
				this.generateClusters();
				clusteringQuality[thresholdIndex] = ClusteringEvaluationController.calculateCCClus(originalCorrelationMatrix, clusters);
				System.out.print(this.clusters.length + "\t");

				thresholdIndex--;
				if (thresholdIndex >= 0)
					this.threshold = thresholdSet[thresholdIndex];

				leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, numDocuments, false);
			}
		}

		double bestThreshold = 0D;
		double bestQuality = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < thresholdSet.length; i++)
			if (clusteringQuality[i] > bestQuality) {
				bestThreshold = thresholdSet[i];
				bestQuality = clusteringQuality[i];
			}

		System.out.println("Best threshold: " + bestThreshold);
		return bestThreshold;
	}

	class Vertex {
		int index;
		int rank;
		Vertex parent;

		public Vertex(int index) {
			this.index = index;
			this.rank = 0;
			this.parent = this;
		}

		public Vertex getParent() {
			return parent;
		}

		public void setParent(Vertex parent) {
			this.parent = parent;
		}

		@Override
		public boolean equals(Object obj) {
			return this.index == ((Vertex) obj).index;
		}

		@Override
		public int hashCode() {
			return this.index;
		}
	}

	class DisjointTree {
		Vertex[] vertexMapping = new Vertex[numDocuments];

		public void makeSet(Vertex v) {
			vertexMapping[v.index] =  v;
		}

		public Vertex findSet(int v) {
			Vertex mapped = vertexMapping[v];

			if (mapped == null)
				return null;

			Vertex parent = mapped.getParent();
			if (v != parent.index) {
				mapped.setParent(findSet(parent.index));
			}

			return mapped.getParent();
		}

		public void union(Vertex v1, Vertex v2) {
			Vertex set1 = findSet(v1.index);
			Vertex set2 = findSet(v2.index);

			if (set1 == null || set2 == null || set1.equals(set2))
				return;

			Vertex mapped1 = vertexMapping[set1.index];
			Vertex mapped2 = vertexMapping[set2.index];

			if (mapped1.rank > mapped2.rank) {
				mapped2.parent = v1;
			} else {
				mapped1.parent = v2;
				if (mapped1.rank == mapped2.rank) mapped2.rank++;
			}
		}
	}
	
	public class Pair{
	    private int first;
	    private int second;

	    public Pair(int first, int second) {
	    	super();
	    	this.first = first;
	    	this.second = second;
	    }

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Pair pair = (Pair) o;

			return first == pair.first && second == pair.second;
		}

		@Override
		public int hashCode() {
			int result = first;
			result = 31 * result + second;
			return result;
		}
	}
}