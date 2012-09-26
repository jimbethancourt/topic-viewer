package br.ufmg.aserg.topicviewer.correlation;

import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrix {
	
	protected Map<String, Integer> xEntityIds;
	protected Map<String, Integer> yEntityIds;
	
	protected DoubleMatrix2D correlationMatrix;
	
	public CorrelationMatrix(Map<String, Integer> xEntityIds, Map<String, Integer> yEntityIds, DoubleMatrix2D matrix) {
		this.xEntityIds = xEntityIds;
		this.yEntityIds = yEntityIds;
		this.correlationMatrix = matrix;
	}

	public Map<String, Integer> getXEntityIds() {
		return xEntityIds;
	}

	public Map<String, Integer> getyEntityIds() {
		return yEntityIds;
	}

	public DoubleMatrix2D getCorrelationMatrix() {
		return correlationMatrix;
	}
}