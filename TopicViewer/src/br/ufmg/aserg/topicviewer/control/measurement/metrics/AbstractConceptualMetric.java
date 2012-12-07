package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

public abstract class AbstractConceptualMetric {

	protected double similarityThreshold = 0.7;
	
	protected String acronym;
	
	protected Map<String, List<Integer>> packageMapping;
	protected DoubleMatrix2D termDocMatrix;
	
	public AbstractConceptualMetric(String acronym, DoubleMatrix2D termDocMatrix, String[] documentIds) {
		this.termDocMatrix = termDocMatrix;
		this.buildPackageMapping(documentIds);
	}
	
	private void buildPackageMapping(String[] documentIds) {
		this.packageMapping = new HashMap<String, List<Integer>>();
		
		for (int i = 0; i < documentIds.length; i++) {
			String document = documentIds[i];
			String packageName = document.substring(document.lastIndexOf(':')+1, document.lastIndexOf('.'));
			putNewDocument(packageName, i);
		}
	}
	
	private void putNewDocument(String packageName, Integer documentId) {
		List<Integer> classes = (this.packageMapping.containsKey(packageName)) ? 
				this.packageMapping.get(packageName) : 
				new ArrayList<Integer>();
		classes.add(documentId);
				
		this.packageMapping.put(packageName, classes);
	}
	
	protected abstract double calculate(String packageName);
	
	public Map<String, Double> calculate() {
		Map<String, Double> metricMapping = new HashMap<String, Double>();
		for (String packageName : this.packageMapping.keySet())
			metricMapping.put(packageName, calculate(packageName));
		return metricMapping;
	}
	
	protected double calculateSimilarity(int document1, int document2) {
		DoubleMatrix1D vector1 = this.termDocMatrix.viewColumn(document1);
		DoubleMatrix1D vector2 = this.termDocMatrix.viewColumn(document2);
		
		double cosineSimilarity = vector1.zDotProduct(vector2);
		double denominator = Math.sqrt(vector1.zDotProduct(vector1) * vector2.zDotProduct(vector2));
		return denominator == 0 ? 50000 : cosineSimilarity / denominator;
	}
}