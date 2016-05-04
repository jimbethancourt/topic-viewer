package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;

public class HierarchicalClustering {

	private int numDocuments;
	private int minClusters;
	private double threshold;
	
	private DisjointTree clustersTree;
	
	private int[][] clusters;
	
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
		
		int[] leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, true);
		while (this.minClusters < numClusters && leastDissimilarPair != null) {
			Vertex set1 = this.clustersTree.findSet(leastDissimilarPair[0]);
			Vertex set2 = this.clustersTree.findSet(leastDissimilarPair[1]);
			
			if (!set1.equals(set2)) {
				this.clustersTree.union(set1, set2);
				updateCorrelationMatrix(set1.index, correlationMatrix);
				numClusters--;
			}
			
			leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, false);
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
	
	private int[] getLeastDissimilarPair(DoubleMatrix2D correlationMatrix, boolean force) {
		double bestSimilarity = Double.NEGATIVE_INFINITY;
		int[] leastDissimilarPair = {0,0};
		
		for (int i = 0; i < this.numDocuments; i++)
			for (int j = 0; j < this.numDocuments; j++) {
				double similarity = correlationMatrix.get(i, j);
				if (i < j && similarity != Double.NEGATIVE_INFINITY && similarity > bestSimilarity) {
					bestSimilarity = similarity;
					leastDissimilarPair = new int[] {i,j};
				}
			}
		
		return (bestSimilarity < this.threshold) ? (force ? leastDissimilarPair : null) : leastDissimilarPair;
	}
	
	// implementing average linkage
	private void updateCorrelationMatrix(int unionSet, DoubleMatrix2D correlationMatrix) {
		int union = this.clustersTree.findSet(unionSet).index;
		
		Set<Pair<Integer, Integer>> calculatedClusters = new HashSet<Pair<Integer, Integer>>();
		for (int i = 0; i < correlationMatrix.rows()-1; i++)
			for (int j = i+1; j < correlationMatrix.rows(); j++) {
				int set1Index = this.clustersTree.findSet(i).index;
				int set2Index = this.clustersTree.findSet(j).index;
				
				Pair<Integer, Integer> newPair = (set1Index < set2Index) ? 
						new Pair<Integer, Integer>(set1Index, set2Index) : 
						new Pair<Integer, Integer>(set2Index, set1Index);
						
				if ((set1Index == union || set2Index == union) && !calculatedClusters.contains(newPair)) {
					double newValue;
					Set<Integer> set1 = getClusterSet(set1Index);
					Set<Integer> set2 = getClusterSet(set2Index);
					
					if (set1Index == set2Index) newValue = Double.NEGATIVE_INFINITY;
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
					
					calculatedClusters.add(newPair);
				}
			}
	}
	
	private Set<Integer> getClusterSet(int rootIndex) {
		Set<Integer> clusterSet = new HashSet<Integer>();
		for (int i = 0; i < this.numDocuments; i++)
			if (this.clustersTree.findSet(i).index == rootIndex)
				clusterSet.add(i);
		return clusterSet;
	}
	
	public double getBestThreshold(DoubleMatrix2D correlationMatrix) throws IOException {
		final double[] thresholdSet = {.55, .60, .65, .70, .75};
		double[] clusteringQuality = new double[thresholdSet.length];
		int thresholdIndex = thresholdSet.length-1;
		
		DoubleMatrix2D originalCorrelationMatrix = correlationMatrix.copy();
		
		int[] leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, true);
		while (leastDissimilarPair != null && thresholdIndex >= 0) {
			this.threshold = thresholdSet[thresholdIndex];
			
			Vertex set1 = this.clustersTree.findSet(leastDissimilarPair[0]);
			Vertex set2 = this.clustersTree.findSet(leastDissimilarPair[1]);
			
			if (!set1.equals(set2)) {
				this.clustersTree.union(set1, set2);
				updateCorrelationMatrix(set1.index, correlationMatrix);
			}
			
			leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, false);
			
			if (leastDissimilarPair == null && thresholdIndex >= 0) {
				this.generateClusters();
				clusteringQuality[thresholdIndex] = ClusteringEvaluationController.calculateCCClus(originalCorrelationMatrix, clusters);
				System.out.print(this.clusters.length + "\t");
				
				thresholdIndex--;
				if (thresholdIndex >= 0) this.threshold = thresholdSet[thresholdIndex];
				
				leastDissimilarPair = getLeastDissimilarPair(correlationMatrix, false);
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
			Vertex set1 = findSet(v1.index); Vertex set2 = findSet(v2.index);
			if (set1 == null || set2 == null || set1.equals(set2)) return;
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
	
	public class Pair<A, B> {
	    private A first;
	    private B second;

	    public Pair(A first, B second) {
	    	super();
	    	this.first = first;
	    	this.second = second;
	    }

	    public int hashCode() {
	    	int hashFirst = first != null ? first.hashCode() : 0;
	    	int hashSecond = second != null ? second.hashCode() : 0;
	    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
	    }

	    @SuppressWarnings("rawtypes")
		public boolean equals(Object other) {
	    	if (other instanceof Pair) {
	    		Pair otherPair = (Pair) other;
	    		return 
	    		((  this.first== otherPair.first ||
	    			( this.first != null && otherPair.first != null &&
	    			  this.first.equals(otherPair.first))) &&
	    		 (	this.second == otherPair.second ||
	    			( this.second != null && otherPair.second != null &&
	    			  this.second.equals(otherPair.second))) );
	    	}

	    	return false;
	    }
	}
}