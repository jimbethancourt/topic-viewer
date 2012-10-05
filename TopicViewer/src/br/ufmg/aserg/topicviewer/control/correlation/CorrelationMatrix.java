package br.ufmg.aserg.topicviewer.control.correlation;

import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrix {
	
	private String[] entityIds;
	
	protected DoubleMatrix2D correlationMatrix;
	
	public CorrelationMatrix(String[] entityIds, DoubleMatrix2D matrix) {
		this.entityIds = entityIds;
		this.correlationMatrix = matrix;
	}

	public DoubleMatrix2D getCorrelationMatrix() {
		return correlationMatrix;
	}
	
	public Integer getNumEntities() {
		return this.entityIds.length;
	}
}