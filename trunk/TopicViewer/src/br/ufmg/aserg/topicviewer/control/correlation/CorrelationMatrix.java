package br.ufmg.aserg.topicviewer.control.correlation;

import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrix {
	
	protected Map<String, Integer> entityIds;
	
	protected DoubleMatrix2D correlationMatrix;
	
	public CorrelationMatrix(Map<String, Integer> entityIds, DoubleMatrix2D matrix) {
		this.entityIds = entityIds;
		this.correlationMatrix = matrix;
	}

	public DoubleMatrix2D getCorrelationMatrix() {
		return correlationMatrix;
	}
}