package br.ufmg.aserg.topicviewer.control.semantic;

import java.util.HashSet;
import java.util.Set;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.jet.math.PlusMult;

public class SemanticTopicsCalculator {
	
	public static String[][] generateSemanticTopics(int[][] clusters, DoubleMatrix2D termDocMatrix, String[] termIds) {
		String[][] topics = new String[clusters.length][0];
		final DoubleDoubleFunction sumFunction = PlusMult.plusMult(1);
		final DoubleDoubleFunction relevanceFunction = PlusMult.plusDiv(clusters.length-1);
		
		for (int i = 0; i < clusters.length; i++) {
			DoubleMatrix1D termRelevance = new SparseDoubleMatrix1D(termIds.length);
			DoubleMatrix1D termFrequency = new SparseDoubleMatrix1D(termIds.length);
			for (int j = 0; j < termDocMatrix.columns(); j++)
				if (containsDocument(j, clusters[i]))
					termRelevance.assign(termDocMatrix.viewColumn(j), sumFunction);
				else termFrequency.assign(termDocMatrix.viewColumn(j), sumFunction);
			
			termRelevance.assign(termFrequency, relevanceFunction);
			
			int[] topicIds = getMostRelevantTerms(termRelevance, termDocMatrix);
			
			String[] topic = new String[topicIds.length];
			for (int j = 0; j < topicIds.length; j++)
				topic[j] = termIds[topicIds[j]];
			topics[i] = topic;
		}
		
		return topics;
	}
	
	private static boolean containsDocument(int docId, int[] cluster) {
		for (int document : cluster)
			if (document == docId) return true;
		return false;
	}
	
	private static int[] getMostRelevantTerms(final DoubleMatrix1D termFrequency, DoubleMatrix2D termDocMatrix) {
		final int numSelectedTerms = 7;
		
		int[] relevantTerms = new int[numSelectedTerms];
		int newTermIndex = 0;
		
		Set<Integer> visitedTerms = new HashSet<Integer>(); 
		while (newTermIndex < numSelectedTerms && visitedTerms.size() < termFrequency.size()) {
			
			int maxFrequencyIndex = -1;
			double maxFrequency = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < termFrequency.size(); i++)
				if (!visitedTerms.contains(i) && termFrequency.get(i) > maxFrequency) {
					maxFrequencyIndex = i;
					maxFrequency = termFrequency.get(i);
				}
			
			if (maxFrequencyIndex != -1) {
				relevantTerms[newTermIndex] = maxFrequencyIndex;
				newTermIndex++;
			}
			
			visitedTerms.add(maxFrequencyIndex);
		}
		
		return relevantTerms;
	}
}