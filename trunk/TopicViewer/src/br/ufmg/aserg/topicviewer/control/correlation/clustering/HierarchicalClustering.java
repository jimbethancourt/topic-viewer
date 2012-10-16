package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import cern.colt.matrix.DoubleMatrix2D;

public class HierarchicalClustering {

	private int numDocuments;
	private int maxClusters;
	private DisjointTree clustersTree;
	private AgglomerativeLinkage linkage;
	
	private DoubleMatrix2D clusteredMatrix;
	private DoubleMatrix2D clusteredWithLinksMatrix;
	private Map<Integer, Integer> indexMapping;
	private int[][] clusters;
	
	public HierarchicalClustering(CorrelationMatrix correlationMatrix, int numClusters) {
		this.numDocuments = correlationMatrix.getNumEntities();
		this.maxClusters = numClusters;
		this.clustersTree = new DisjointTree();
		this.linkage = createLinkage();
		DoubleMatrix2D correlationMatrix2D = correlationMatrix.getCorrelationMatrix();
		
		int[] documents = new int[numDocuments];
		for (int i = 0; i < documents.length; i++) {
			documents[i] = i;
			clustersTree.makeSet(new Vertex(i));
		}
		
		initClustering(correlationMatrix2D.copy());
		this.indexMapping = ClusteredMatrixCalculator.generateIndexMapping(this.clusters);
		this.clusteredMatrix = ClusteredMatrixCalculator.generateClusteredMatrix(correlationMatrix2D, this.clusters, this.indexMapping);
		this.clusteredWithLinksMatrix = ClusteredMatrixCalculator.generateClusteredWithLinksMatrix(correlationMatrix2D, this.clusteredMatrix, this.indexMapping);
	}
	
	public DoubleMatrix2D getClusteredMatrix() {
		return this.clusteredMatrix;
	}
	
	public DoubleMatrix2D getClusteredWithLinksMatrix() {
		return this.clusteredWithLinksMatrix;
	}
	
	public Map<Integer, Integer> getIndexMapping() {
		return this.indexMapping;
	}
	
	public int[][] getClusters() {
		return this.clusters;
	}
	
	private void initClustering(DoubleMatrix2D correlationMatrix) {
		int numClusters = this.numDocuments;
		
		while (this.maxClusters < numClusters) {
			int[] leastDissimilarPair = getLeastDissimilarPair(correlationMatrix);
			Vertex set1 = this.clustersTree.findSet(new Vertex(leastDissimilarPair[0]));
			Vertex set2 = this.clustersTree.findSet(new Vertex(leastDissimilarPair[1]));
			
			if (set1 != set2) {
				this.clustersTree.union(set1, set2);
				updateCorrelationMatrix(set1.index, set2.index, correlationMatrix);
				numClusters--;
			}
		}
		
		this.generateClusters();
	}
	
	private void generateClusters() {
		Map<Integer, List<Integer>> mapping = new HashMap<Integer, List<Integer>>();
		
		for (int i = 0; i < this.numDocuments; i++) {
			Vertex set = this.clustersTree.findSet(new Vertex(i));
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
	
	private int[] getLeastDissimilarPair(DoubleMatrix2D correlationMatrix) {
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
		
		return leastDissimilarPair;
	}
	
	private void updateCorrelationMatrix(int v1, int v2, DoubleMatrix2D correlationMatrix) {
		Set<Integer> union = new HashSet<Integer>(); int set1 = 0, set2 = 0;
		for (int i = 0; i < correlationMatrix.rows(); i++) {
			int set = this.clustersTree.findSet(new Vertex(i)).index;
			if (set == v1 || set == v2) union.add(i);
			if (set == v1) set1++;
			if (set == v2) set2++;
		}
		
		for (Integer i : union)
			for (int j = 0; j < correlationMatrix.rows(); j++) {
				double newValue;
				if (union.contains(j)) newValue = Double.NEGATIVE_INFINITY;
				else {
					double distance1 = correlationMatrix.get(v1, j);
					double distance2 = correlationMatrix.get(v2, j);
					newValue = this.linkage.getNewDistance(set1, set2, distance1, distance2);
				}
				correlationMatrix.set(i, j, newValue); correlationMatrix.set(j, i, newValue);
			}
	}
	
	protected AgglomerativeLinkage createLinkage() {
		return new AgglomerativeLinkage() {
			@Override
			public double getNewDistance(int set1, int set2, double distance1, double distance2) {
				double numerator = (set1 * distance1) + (set2 * distance2); 
				double denominator = set1 + set2;
				return (numerator / denominator);
			}
		};
	}
	
	interface AgglomerativeLinkage {
		public double getNewDistance(int setSize1, int setSize2, double distance1, double distance2);
	}

	static class Vertex {
		int index;
		int rank;
		Vertex parent;
		
		public Vertex(int index) {
			this.index = index;
			this.rank = 0;
			this.parent = this;
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
	
	static class DisjointTree {
		Map<Vertex, Vertex> vertexMapping = new HashMap<Vertex, Vertex>();
		
		public void makeSet(Vertex v) {
			vertexMapping.put(v, v);
		}
		
		public Vertex findSet(Vertex v) {
			Vertex mapped = vertexMapping.get(v);
			if (mapped == null) return null;
			if (v != mapped.parent) mapped.parent = findSet(mapped.parent);
			return mapped.parent;
		}
		
		public void union(Vertex v1, Vertex v2) {
			Vertex set1 = findSet(v1); Vertex set2 = findSet(v2);
			if (set1 == null || set2 == null || set1 == set2) return;
			Vertex mapped1 = vertexMapping.get(set1);
			Vertex mapped2 = vertexMapping.get(set2);
			
			if (mapped1.rank > mapped2.rank) {
				mapped2.parent = v1;
			} else {
				mapped1.parent = v2;
				if (mapped1.rank == mapped2.rank) mapped2.rank++;
			}
		}
	}
}