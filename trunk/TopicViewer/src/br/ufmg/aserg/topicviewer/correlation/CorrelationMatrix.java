package br.ufmg.aserg.topicviewer.correlation;

import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrix {
	
	protected Map<String, Integer> xEntityIds;
	protected Map<String, Integer> yEntityIds;
	
	protected DoubleMatrix2D correlationMatrix;
	
	

}