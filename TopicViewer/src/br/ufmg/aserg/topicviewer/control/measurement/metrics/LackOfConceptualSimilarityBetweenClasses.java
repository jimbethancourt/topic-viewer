package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;

public class LackOfConceptualSimilarityBetweenClasses extends AbstractConceptualMetric {

	public LackOfConceptualSimilarityBetweenClasses(DoubleMatrix2D termDocMatrix, String[] documentIds) {
		super("LCSC", termDocMatrix, documentIds);
	}
	
	public LackOfConceptualSimilarityBetweenClasses(AbstractConceptualMetric metric) {
		super("LCSC", metric);
	}

	@Override
	protected double calculate(String packageName) {
		List<Integer> classes = this.packageMapping.get(packageName);
		int numClasses = classes.size();
		
		// calculate all similarities
		double[][] similarities = new double[numClasses][numClasses];
		for (int i = 0; i < numClasses; i++)
			for (int j = i+1; j < numClasses; j++)
				similarities[i][j] = similarities[j][i] = calculateSimilarity(classes.get(i), classes.get(j));
		
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
		
		return (noInterset-intersect) / (noInterset+intersect);
	}
	
	private boolean intersect(List<Integer> set1, List<Integer> set2) {
		for (Integer element1 : set1)
			if (set2.contains(element1))
				return true;
		return false;
	}
}