package br.ufmg.aserg.topicviewer.control.correlation;

import java.io.File;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.doublealgo.Statistic;
import cern.colt.matrix.doublealgo.Statistic.VectorVectorFunction;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class CorrelationMatrixController extends AbstractController {
	
	private File[] matrixFiles;
	
	public CorrelationMatrixController(File[] matrixFiles) {
		super();
		
		this.matrixFiles = matrixFiles;
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.CORRELATION_MATRIX_OUTPUT;
		this.checkResultFolder();
	}
	
	private DoubleMatrix2D buildCorrelationMatrix(DoubleMatrix2D termDocumentMatrix) {
		int numDocuments = termDocumentMatrix.columns();
		VectorVectorFunction euclideanFunction = Statistic.EUCLID;
		DoubleMatrix2D correlationMatrix = new DenseDoubleMatrix2D(numDocuments, numDocuments);
		
		for (int i = 0; i < numDocuments; i++)
			for (int j = 0; j < numDocuments; j++) {
				DoubleMatrix1D document1 = termDocumentMatrix.viewColumn(i);
				DoubleMatrix1D document2 = termDocumentMatrix.viewColumn(j);
				
				double correlation = euclideanFunction.apply(document1, document2);
				correlationMatrix.set(i, j, correlation);
			}
		
		return correlationMatrix;
	}

	@Override
	public void run() {
		for (File matrixFile : this.matrixFiles) {
			try {
				String projectName = (matrixFile.getName().contains("-lsi")) 
						? matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('-'))
						: matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('.'));
				
				DoubleMatrix2D termDocumentMatrix = FileUtilities.readMatrix(matrixFile.getAbsolutePath());
				DoubleMatrix2D correlationMatrix2d = this.buildCorrelationMatrix(termDocumentMatrix);
				
				FileUtilities.saveMatrix(correlationMatrix2d, this.resultFolderName + File.separator + projectName + ".matrix");
			} catch (Exception e) {
				this.failedProjects.add(matrixFile);
			}
			
			this.addAnalyzedProject();
		}
	}
}