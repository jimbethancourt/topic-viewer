package br.ufmg.aserg.topicviewer.control.semantic;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.splabs.vocabulary.filter.IdentifierFilter;

import ptstemmer.exceptions.PTStemmerException;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.Properties;
import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.PlusMult;

public class SemanticTopicsCalculator {
	
	private static final int NUM_SELECTED_TERMS = 10;
	
	public static String[][] generateSemanticTopicsFromVocabulary(int[][] clusters, DoubleMatrix2D lsiTermDocMatrix, DoubleMatrix2D lsiTransform, String[] termIds, boolean mostRelevant) throws IOException {
		
		Algebra matrixAlgebra = Algebra.ZERO;
		
		final int numTerms = termIds.length;
		final int numClusters = clusters.length;
		cern.colt.matrix.DoubleMatrix2D lsiTransformCopy = getLsiTransformCopy(lsiTransform);
		
		DoubleMatrix2D clusterSimilarity = new DoubleMatrix2D(numTerms, numClusters);
		for (int i = 0; i < numTerms; i++) {
			DoubleMatrix1D termQuery = new SparseDoubleMatrix1D(numTerms); termQuery.set(i, 1D);
			termQuery = matrixAlgebra.mult(lsiTransformCopy, termQuery);
			
			for (int j = 0; j < numClusters; j++) {
				double clusterSize = clusters[j].length;
				double similarity = 0D;
				double avgSimilarity = 0D;
				
				for (int documentId : clusters[j]) {
					similarity = calculateSimilarity(termQuery, lsiTermDocMatrix.viewColumn(documentId));
					if (Double.isNaN(similarity)) clusterSize--;
					avgSimilarity += (Double.isNaN(similarity) ? 0D : similarity);
				}
				
				clusterSimilarity.set(i, j, clusterSize == 0D ? 0D : avgSimilarity / clusterSize);
			}
		}
		
		
		if (mostRelevant)
			clusterSimilarity = getRelevanceMatrix(numTerms, numClusters, clusterSimilarity);
		
		return getBestTerms(clusterSimilarity, termIds);
	}
	
	public static String[][] generateSemanticTopicsFromClasses(int[][] clusters, String[] termIds, String[] documentIds) throws IOException, PTStemmerException {
		
		IdentifierFilter filter = new IdentifierFilter(Properties.getProperties());
		
		final int numTerms = termIds.length;
		final int numClusters = clusters.length;
		
		DoubleMatrix2D similarityMatrix = new DoubleMatrix2D(numTerms, numClusters);
		for (int i = 0; i < clusters.length; i++)
			for (int j = 0; j < clusters[i].length; j++) {
				String documentName = documentIds[clusters[i][j]];
				documentName = documentName.substring(documentName.lastIndexOf('.')+1);
				for (String term : filter.filterIdentifiers(new String[] {documentName})) {
					int index = indexOf(term, termIds);
					if (index != -1) similarityMatrix.set(index, i, similarityMatrix.get(index, i) + 1);
				}
			}

		return getBestTerms(similarityMatrix, termIds);
	}
	
	private static int indexOf(String term, String[] termIds) {
		for (int i = 0; i < termIds.length; i++)
			if (termIds[i].equals(term)) return i;
		return -1;
	}
	
	private static DoubleMatrix2D getRelevanceMatrix(int numTerms, int numClusters, DoubleMatrix2D similarityMatrix) throws IOException {
		final DoubleDoubleFunction sumFunction = PlusMult.plusMult(1);
		final DoubleDoubleFunction relevanceFunction = PlusMult.minusDiv(numClusters-1);
		
		DoubleMatrix2D clusterRelevance = new DoubleMatrix2D(numTerms, numClusters);
		for (int i = 0; i < numClusters; i++) {
			DoubleMatrix1D termRelevance = similarityMatrix.viewColumn(i);
			DoubleMatrix1D termInterSimilarity = new DenseDoubleMatrix1D(numTerms);
			
			for (int j = 0; j < numClusters; j++)
				if (i != j)	termInterSimilarity.assign(similarityMatrix.viewColumn(j), sumFunction);
			
			termRelevance.assign(termInterSimilarity, relevanceFunction);
			
			for (int j = 0; j < numTerms; j++)
				clusterRelevance.set(j, i, termRelevance.get(j));
		}
		
		return clusterRelevance;
	}
	
	private static String[][] getBestTerms(DoubleMatrix2D similarityMatrix, String[] termIds) {
		String[][] topics = new String[similarityMatrix.columns()][0];
		
		for (int i = 0; i < similarityMatrix.columns(); i++) {
			int[] topicIds = getMostRelevantTerms(similarityMatrix.viewColumn(i));
			
			String[] topic = new String[NUM_SELECTED_TERMS];
			for (int j = 0; j < NUM_SELECTED_TERMS; j++)
				topic[j] = termIds[topicIds[j]];
			topics[i] = topic;
		}
		
		return topics;
	}
	
	public static String[][] generateSemanticTopics(int[][] clusters, DoubleMatrix2D lsiTermDocMatrix, DoubleMatrix2D lsiTransform, String[] termIds) throws IOException {
		
		Algebra matrixAlgebra = Algebra.ZERO;
		
		final int numTerms = termIds.length;
//		final int numDocuments = lsiTermDocMatrix.columns();
		final int numClusters = clusters.length;
		cern.colt.matrix.DoubleMatrix2D lsiTransformCopy = getLsiTransformCopy(lsiTransform);
		
		DoubleMatrix2D clusterSimilarity = new DoubleMatrix2D(numTerms, numClusters);
		for (int i = 0; i < numTerms; i++) {
			DoubleMatrix1D termQuery = new SparseDoubleMatrix1D(numTerms); termQuery.set(i, 1D);
			termQuery = matrixAlgebra.mult(lsiTransformCopy, termQuery);
			
			for (int j = 0; j < numClusters; j++) {
				double clusterSize = clusters[j].length;
				double similarity = 0D;
				double avgSimilarity = 0D;
				
				for (int documentId : clusters[j]) {
					similarity = calculateSimilarity(termQuery, lsiTermDocMatrix.viewColumn(documentId));
					if (Double.isNaN(similarity)) clusterSize--;
					avgSimilarity += (Double.isNaN(similarity) ? 0D : similarity);
				}
				
				clusterSimilarity.set(i, j, clusterSize == 0D ? 0D : avgSimilarity / clusterSize);
			}
		}
		
		// calculating similarity between terms and documents
//		DoubleMatrix2D documentSimilarity = new DoubleMatrix2D(numTerms, numDocuments); 
//		for (int i = 0; i < numTerms; i++) {
//			DoubleMatrix1D termQuery = new SparseDoubleMatrix1D(numTerms); termQuery.set(i, 1D);
//			
//			for (int j = 0; j < numDocuments; j++) {
//				Double similarity = calculateSimilarity(matrixAlgebra.mult(getLsiTransformCopy(lsiTransform), termQuery), lsiTermDocMatrix.viewColumn(j));
//				documentSimilarity.set(i, j, similarity);
//				
//			}
//		}
//		
//		// calculating similarity between terms and clusters
//		final DoubleDoubleFunction sumFunction = PlusMult.plusMult(1);
//		
//		DoubleMatrix2D clusterSimilarity = new DoubleMatrix2D(numTerms, numClusters);
//		for (int i = 0; i < numClusters; i++) {
//			DoubleMatrix1D similarity = new DenseDoubleMatrix1D(numTerms);
//			
//			for (int documentId : clusters[i])
//				similarity.assign(documentSimilarity.viewColumn(documentId), sumFunction);
//			similarity.assign(Mult.div(clusters[i].length));
//			
//			for (int j = 0; j < numTerms; j++)
//				clusterSimilarity.set(j, i, similarity.get(j));
//		}
		
		// calculating relevance between terms and clusters
//		final DoubleDoubleFunction relevanceFunction = PlusMult.minusDiv(numClusters-1);
//		
//		documentSimilarity = null;
//		DoubleMatrix2D clusterRelevance = new DoubleMatrix2D(numTerms, numClusters);
//		for (int i = 0; i < numClusters; i++) {
//			DoubleMatrix1D termRelevance = clusterSimilarity.viewColumn(i);
//			DoubleMatrix1D termInterSimilarity = new DenseDoubleMatrix1D(numTerms);
//			
//			for (int j = 0; j < numClusters; j++)
//				if (i != j)	termInterSimilarity.assign(clusterSimilarity.viewColumn(j), sumFunction);
//			
//			termRelevance.assign(termInterSimilarity, relevanceFunction);
//			
//			for (int j = 0; j < numTerms; j++)
//				clusterRelevance.set(j, i, termRelevance.get(j));
//		}
		
		// calculating most relevant terms
//		clusterSimilarity = null;
		String[][] topics = new String[numClusters][0];
		for (int i = 0; i < numClusters; i++) {
//			int[] topicIds = getMostRelevantTerms(clusterRelevance.viewColumn(i));
			int[] topicIds = getMostRelevantTerms(clusterSimilarity.viewColumn(i));
			
			String[] topic = new String[NUM_SELECTED_TERMS];
			for (int j = 0; j < NUM_SELECTED_TERMS; j++)
				topic[j] = termIds[topicIds[j]];
			topics[i] = topic;
		}
		
		return topics;
	}
	
	private static double calculateSimilarity(DoubleMatrix1D vector1, DoubleMatrix1D vector2) {
		double cosineSimilarity = vector1.zDotProduct(vector2);
		cosineSimilarity /= Math.sqrt(vector1.zDotProduct(vector1) * vector2.zDotProduct(vector2));
		return cosineSimilarity;
	}
	
	private static int[] getMostRelevantTerms(final DoubleMatrix1D termRelevance) {
		int[] relevantTerms = new int[NUM_SELECTED_TERMS];
		
		int newTermIndex = 0;
		int numTerms = termRelevance.size();
		
		Set<Integer> visitedTerms = new HashSet<Integer>(); 
		while (newTermIndex < NUM_SELECTED_TERMS && visitedTerms.size() < numTerms) {
			
			int maxFrequencyIndex = -1;
			double maxFrequency = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < termRelevance.size(); i++)
				if (!visitedTerms.contains(i) && termRelevance.get(i) > maxFrequency) {
					maxFrequencyIndex = i;
					maxFrequency = termRelevance.get(i);
				}
			
			if (maxFrequencyIndex != -1) {
				relevantTerms[newTermIndex] = maxFrequencyIndex;
				newTermIndex++;
			}
			
			visitedTerms.add(maxFrequencyIndex);
		}
		
		return relevantTerms;
	}
	
	private static cern.colt.matrix.DoubleMatrix2D getLsiTransformCopy(DoubleMatrix2D lsiTransform) {
		cern.colt.matrix.DoubleMatrix2D matrix = new DenseDoubleMatrix2D(lsiTransform.rows(), lsiTransform.columns());
		for (int i = 0; i < lsiTransform.rows(); i++)
    		for (int j = 0; j < lsiTransform.columns(); j++) 
    			matrix.set(i, j, lsiTransform.get(i, j));
		return matrix;
	}
}