package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.io.IOException;

import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;

public class ConceptualCouplingOfPackages extends ConceptualCouplingBetweenPackages {

	public ConceptualCouplingOfPackages(DoubleMatrix2D termDocMatrix, String[] documentIds) throws IOException {
		super("CCoP", termDocMatrix, documentIds);
	}
	
	public ConceptualCouplingOfPackages(AbstractConceptualMetric metric) throws IOException {
		super("CCoP", metric);
	}
	
	@Override
	protected double calculate(String packageName) {
		int packageIndex = this.packageMatrixMapping.get(packageName);
		
		double similarityCount = 0D;
		for (int i = 0; i < this.packages.length; i++)
			similarityCount += similarities[packageIndex][i];
		
		return (double) similarityCount / (this.packages.length - 1);
	}
}