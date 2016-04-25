package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.io.IOException;

import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;

public class ConceptualCouplingBetweenPackages extends AbstractConceptualMetric {
	
	protected String[] packages;
	protected double[][] similarities;

	protected ConceptualCouplingBetweenPackages(String acronym, DoubleMatrix2D termDocMatrix, String[] documentIds) throws IOException {
		super(acronym, termDocMatrix, documentIds);
		
		this.buildPackageTermDocMatrix();
		this.calculateSimilarities();
	}
	
	public ConceptualCouplingBetweenPackages(String acronym, AbstractConceptualMetric metric) throws IOException {
		super("CCBP", metric);
		
		this.buildPackageTermDocMatrix();
		this.calculateSimilarities();
	}
	
	public ConceptualCouplingBetweenPackages(DoubleMatrix2D termDocMatrix, String[] documentIds) throws IOException {
		super("CCBP", termDocMatrix, documentIds);
		
		this.buildPackageTermDocMatrix();
		this.calculateSimilarities();
	}
	
	public ConceptualCouplingBetweenPackages(AbstractConceptualMetric metric) throws IOException {
		super("CCBP", metric);
		
		this.buildPackageTermDocMatrix();
		this.calculateSimilarities();
	}
	
	private void calculateSimilarities() {
		final int packageCount = this.packageMapping.size();
		this.similarities = new double[packageCount][packageCount];
		this.packages = getPackagesInOrder();
		
		for (int i = 0; i < packageCount; i++)
			for (int j = i+1; j < packageCount; j++)
				similarities[i][j] = similarities[j][i] = calculateSimilarity(packages[i], packages[j]);
	}

	@Override
	protected double calculate(String packageName) {
		int packageIndex = this.packageMatrixMapping.get(packageName);
		
		double couplingCount = 0D;
		for (int i = 0; i < this.packages.length; i++)
			if (similarities[packageIndex][i] >= this.similarityThreshold)
				couplingCount++;
		
		return (double) couplingCount / (this.packages.length - 1);
	}
	
	private String[] getPackagesInOrder() {
		final int packages = this.packageMapping.size();
		String[] packagesInOrder = new String[packages];
		for (String packageName : this.packageMapping.keySet())
			packagesInOrder[this.packageMatrixMapping.get(packageName)] = packageName;
		return packagesInOrder;
	}
	
	protected double calculateSimilarity(String document1, String document2) {
		int doc1Index = this.packageMatrixMapping.get(document1);
		int doc2Index = this.packageMatrixMapping.get(document2);
		
		DoubleMatrix1D vector1 = this.packageMatrix.viewColumn(doc1Index);
		DoubleMatrix1D vector2 = this.packageMatrix.viewColumn(doc2Index);
		
		double cosineSimilarity = vector1.zDotProduct(vector2);
		double denominator = Math.sqrt(vector1.zDotProduct(vector1) * vector2.zDotProduct(vector2));
		return denominator == 0 ? 50000 : cosineSimilarity / denominator;
	}
}