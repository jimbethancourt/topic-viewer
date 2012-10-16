package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.util.HashMap;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class ClusteredMatrixCalculator {
	
	public static Map<Integer, Integer> generateIndexMapping(int[][] clusters) {
		Map<Integer, Integer> indexMapping = new HashMap<Integer, Integer>();
		
		int indexCount = 0;
		for (int i = 0; i < clusters.length; i++)
			for (int j = 0; j < clusters[i].length; j++) {
				indexMapping.put(clusters[i][j], indexCount);
				indexCount++;
			}
		
		return indexMapping;
	}
	
	public static DoubleMatrix2D generateClusteredMatrix(DoubleMatrix2D correlationMatrix, int[][] clusters, Map<Integer, Integer> indexMapping) {
		DoubleMatrix2D clusteredMatrix = new DenseDoubleMatrix2D(correlationMatrix.rows(), correlationMatrix.columns());
		
		// intra cluster
		for (int i = 0; i < clusters.length; i++) {
			int[] cluster = clusters[i];
			double clusterSimilarity = calculateMeanSimilarity(correlationMatrix, cluster);
			
			for (int j = 0; j < cluster.length; j++)
				for (int k = 0; k < cluster.length; k++) {
					int newJ = indexMapping.get(cluster[j]);
					int newK = indexMapping.get(cluster[k]);
					
					clusteredMatrix.set(newJ, newK, clusterSimilarity);
					clusteredMatrix.set(newK, newJ, clusterSimilarity);
				}
		}
		
		// inter cluster
		for (int i = 0; i < clusters.length; i++)
			for (int j = 0; j < clusters.length; j++) {
				double clusterSimilarity = calculateMeanSimilarity(correlationMatrix, clusters[i], clusters[j]);
				
				for (int k = 0; k < clusters[i].length; k++) 
					for (int l = 0; l < clusters[j].length; l++) {
						int newK = indexMapping.get(clusters[i][k]);
						int newL = indexMapping.get(clusters[j][l]);
						
						clusteredMatrix.set(newK, newL, clusterSimilarity);
						clusteredMatrix.set(newL, newK, clusterSimilarity);
					}
			}
		
		return clusteredMatrix;
	}
	
	private static double calculateMeanSimilarity(DoubleMatrix2D correlationMatrix, int[] cluster) {
		double sum = 0D;
		
		for (int i = 0; i < cluster.length; i++)
			for (int j = 0; j < cluster.length; j++)
				if (i < j) sum += correlationMatrix.get(i, j);
		
		return sum / ((Math.pow(cluster.length, 2) - cluster.length) / 2);
	}
	
	private static double calculateMeanSimilarity(DoubleMatrix2D correlationMatrix, int[] cluster1, int[] cluster2) {
		double sum = 0D;
		
		for (int i = 0; i < cluster1.length; i++)
			for (int j = 0; j < cluster2.length; j++)
				sum += correlationMatrix.get(i, j);
		
		return sum / (cluster1.length * cluster2.length);
	}
	
	public static DoubleMatrix2D generateClusteredWithLinksMatrix(DoubleMatrix2D correlationMatrix, DoubleMatrix2D clusteredMatrix, Map<Integer, Integer> indexMapping) {
		final double threshold = 0.2;
		DoubleMatrix2D clusteredWithLinksMatrix = clusteredMatrix.copy();
		
		for (int i = 0; i < correlationMatrix.rows(); i++)
			for (int j = 0; j < correlationMatrix.rows(); j++)
				if (i != j) {
					int newI = indexMapping.get(i);
					int newJ = indexMapping.get(j);
					
					double correlationValue = correlationMatrix.get(i, j);
					double clusteredValue = clusteredMatrix.get(newI, newJ);
					if (getPercentageDifference(correlationValue, clusteredValue) > threshold) {
						if (correlationValue > clusteredValue) 
							clusteredValue += threshold;
						else clusteredValue -= threshold;
						
						clusteredWithLinksMatrix.set(newI, newJ, clusteredValue);
					}
				}
		
		return clusteredWithLinksMatrix;
	}
	
	private static double getPercentageDifference(double correlationValue, double clusteredValue) {
		return Math.abs((correlationValue - clusteredValue) / correlationValue);
	}
}