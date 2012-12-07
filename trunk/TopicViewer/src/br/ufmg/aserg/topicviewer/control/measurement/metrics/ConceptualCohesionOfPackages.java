package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;

public class ConceptualCohesionOfPackages extends AbstractConceptualMetric {

	public ConceptualCohesionOfPackages(DoubleMatrix2D termDocMatrix, String[] documentIds) {
		super("CCP", termDocMatrix, documentIds);
	}

	@Override
	protected double calculate(String packageName) {
		List<Integer> classes = this.packageMapping.get(packageName);
		int numClasses = classes.size();
		
		double numCombinations = 0D;
		double similarity = 0D;
		for (int i = 0; i < numClasses; i++) {
			for (int j = i+1; j < numClasses; j++) {
				similarity += calculateSimilarity(classes.get(i), classes.get(j));
				numCombinations++;
			}
		}
		
		double cohesion = similarity / numCombinations;
		return cohesion > 0 ? cohesion : 0;
	}
}