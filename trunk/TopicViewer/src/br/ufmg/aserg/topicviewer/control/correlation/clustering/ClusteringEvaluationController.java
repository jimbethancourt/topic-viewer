package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix1D;

import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;

public class ClusteringEvaluationController {
	
	/*
	 * Calculates RMD metric. RMD stands for Relative Module Dispersion, which
	 * is associated with the whole clustering and calculated by the formula:
	 * (CMmax - CMmin) / AMM, where AMM - Average Module Membership is calcula
	 * ted as numClasses / numClusters
	 */
	private static double calculateRMD(int[][] clusters) {
		double numClasses = 0D;
		double numClusters = clusters.length;
		double cmMin = Double.POSITIVE_INFINITY;
		double cmMax = Double.NEGATIVE_INFINITY;
		
		for (int[] cluster : clusters) {
			cmMin = Math.min(cmMin, cluster.length);
			cmMax = Math.max(cmMax, cluster.length);
			
			numClasses += cluster.length;
		}
		
		double amm = numClasses / numClusters;
		return (cmMax - cmMin) / amm;
	}
	
	/*
	 * Calculates NED metric. NED stands for Non-Extreme Distribution, which is
	 * associated with the whole clustering and calculated by the formula:
	 * numClassesNonExtreme / numClasses, where numClassesNonExtreme is calculated
	 * by the number of classes of clusters whose size is between length thresholds
	 */
	private static double calculateNED(int[][] clusters) {
		double numClasses = 0D;
		double numClusters = clusters.length;
		for (int[] cluster : clusters)
			numClasses += cluster.length;
		
		double meanClusterSize = numClasses / numClusters; 
		double inferiorThreshold = meanClusterSize / 2;
		double superiorThreshold = meanClusterSize * 1.5;
		
		double numClassesNonExtreme = 0D;
		for (int[] cluster : clusters)
			if (cluster.length > inferiorThreshold && cluster.length < superiorThreshold)
				numClassesNonExtreme += cluster.length;
		
		return numClassesNonExtreme / numClasses;
	}
	
	/*
	 * Calculates CCP metric. CCP stands for Conceptual Cohesion of Packages, which
	 * is associated to each module of the clustering and calculated as the average
	 * similarity between each pair of classes of one cluster. We then calculate the
	 * average CCP of all clusters as the CCP of the clustering
	 */
	private static double calculateCCP(DoubleMatrix2D termDocMatrix, int[][] clusters) {
		double ccp = 0D;
		double numClusters = clusters.length;
		
		for (int[] cluster : clusters) {
			double numClasses = cluster.length;
			
			double numCombinations = 0D;
			double similarity = 0D;
			for (int i = 0; i < numClasses; i++) {
				for (int j = i+1; j < numClasses; j++) {
					similarity += calculateSimilarity(termDocMatrix, cluster[i], cluster[j]);
					numCombinations++;
				}
			}
			
			double cohesion = similarity / numCombinations;
			ccp += (cohesion > 0 ? cohesion : 0);
		}
		
		return ccp / numClusters;
	}
	
	/*
	 * Calculates LCSC metric. LCSC stands for Lack of Conceptual Similarity Between
	 * Classes, which is associated to each module of the clustering and calculated
	 * as the ratio of the number of pairs of classes whose similarity was above the
	 * mean similarity of their cluster
	 * */
	private static double calculateLCSC(DoubleMatrix2D termDocMatrix, int[][] clusters) {
		double lcsc = 0D;
		double numClusters = clusters.length;
		
		for (int[] cluster : clusters) {
			int numClasses = cluster.length;
			
			// calculate all similarities
			double[][] similarities = new double[numClasses][numClasses];
			for (int i = 0; i < numClasses; i++)
				for (int j = i+1; j < numClasses; j++)
					similarities[i][j] = similarities[j][i] = calculateSimilarity(termDocMatrix, cluster[i], cluster[j]);
			
			// calculate mean similarity
			double[] meanSimilarities = new double[numClasses];
			for (int i = 0; i < numClasses; i++) {
				double similarity = 0D;
				for (int j = 0; j < numClasses; j++)
					similarity += similarities[i][j];
				meanSimilarities[i] = similarity / (numClasses-1);
			}
			
			// calculate classes above mean
			Map<Integer, List<Integer>> C = new HashMap<Integer, List<Integer>>();
			for (int i = 0; i < numClasses; i++) C.put(i, new LinkedList<Integer>());
			for (int i = 0; i < numClasses; i++)
				for (int j = 0; j < numClasses; j++)
					if (similarities[i][j] > meanSimilarities[i])
						C.get(i).add(j);
			
			// calculate intersection
			similarities = null; meanSimilarities = null;
			int intersect = 0;
			int noInterset = 0;
			for (int i = 0; i < numClasses; i++)
				for (int j = i+1; j < numClasses; j++) {
					if (intersect(C.get(i), C.get(j))) intersect++;
					else noInterset++;
				}
		
			lcsc += (noInterset+intersect) == 0 ? 0D : Math.abs(noInterset-intersect) / (noInterset+intersect);
		}
		
		return lcsc / numClusters;
	}
	
	private static double calculateSimilarity(final DoubleMatrix2D termDocMatrix, int document1, int document2) {
		DoubleMatrix1D vector1 = termDocMatrix.viewColumn(document1);
		DoubleMatrix1D vector2 = termDocMatrix.viewColumn(document2);
		
		double cosineSimilarity = vector1.zDotProduct(vector2);
		double denominator = Math.sqrt(vector1.zDotProduct(vector1) * vector2.zDotProduct(vector2));
		return denominator == 0 ? 1D : cosineSimilarity / denominator;
	}
	
	private static boolean intersect(List<Integer> set1, List<Integer> set2) {
		for (Integer element1 : set1)
			if (set2.contains(element1))
				return true;
		return false;
	}
	
	public static void calculateQualityMetrics(DoubleMatrix2D termDocMatrix, int[][] clusters) {
		System.out.print(calculateRMD(clusters) + "\t");
		System.out.print(calculateNED(clusters) + "\t");
		System.out.print(calculateCCP(termDocMatrix, clusters) + "\t");
		System.out.println(calculateLCSC(termDocMatrix, clusters));
	}
}