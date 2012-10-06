package br.ufmg.aserg.topicviewer.control.correlation;

import java.security.InvalidParameterException;

import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrix {
	
	private String[] entityIds;
	private DoubleMatrix2D correlationMatrix;
	
	public CorrelationMatrix(String[] entityIds, DoubleMatrix2D matrix) {
		if (matrix.rows() != matrix.columns())
			throw new InvalidParameterException("Correlation matrix must be square.");
		
		if (entityIds.length != matrix.rows())
			throw new InvalidParameterException("Ids File must have the same number of entities than the correlation matrix bound (rows or columns).");
		
		this.entityIds = entityIds;
		this.correlationMatrix = matrix;
	}

	public DoubleMatrix2D getCorrelationMatrix() {
		return this.correlationMatrix;
	}
	
	public Double getValueAt(int i, int j) {
		return this.correlationMatrix.get(i, j);
	}
	
	public String getIdAt(int i) {
		return this.entityIds[i];
	}
	
	public Integer getNumEntities() {
		return this.entityIds.length;
	}
}