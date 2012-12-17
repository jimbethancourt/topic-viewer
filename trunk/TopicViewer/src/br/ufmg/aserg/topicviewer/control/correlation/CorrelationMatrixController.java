package br.ufmg.aserg.topicviewer.control.correlation;

import java.io.File;
import java.io.FilenameFilter;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class CorrelationMatrixController extends AbstractController {
	
	private File[] matrixFiles;
	
	public CorrelationMatrixController(File[] matrixFiles) {
		super();
		
		this.matrixFiles = matrixFiles;
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.CORRELATION_MATRIX_OUTPUT;
		this.checkResultFolder();
		
		this.setAllProjectCount(matrixFiles.length);
	}
	
	private DoubleMatrix2D buildCorrelationMatrix(DoubleMatrix2D termDocumentMatrix) {
		int numDocuments = termDocumentMatrix.columns();
		
		DoubleMatrix2D correlationMatrix = new DenseDoubleMatrix2D(numDocuments, numDocuments);
		
		for (int i = 0; i < numDocuments; i++)
			correlationMatrix.set(i, i, 1);
		
		for (int i = 0; i < numDocuments; i++)
			for (int j = 0; j < numDocuments; j++)
				if (j > i) { 
					DoubleMatrix1D document1 = termDocumentMatrix.viewColumn(i);
					DoubleMatrix1D document2 = termDocumentMatrix.viewColumn(j);
					
					double correlation = getCosineDistance(document1, document2);
					correlationMatrix.set(i, j, correlation);
					correlationMatrix.set(j, i, correlation);
				}
		
		return correlationMatrix;
	}
	
	private static double getCosineDistance(DoubleMatrix1D vector1, DoubleMatrix1D vector2) {
		double cosineSimilarity = vector1.zDotProduct(vector2);
		double denominator = Math.sqrt(vector1.zDotProduct(vector1) * vector2.zDotProduct(vector2));
		return denominator == 0 ? 50000 : cosineSimilarity / denominator;
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
				
				String idsFileName = ((matrixFile.getAbsolutePath().contains("-lsi")) 
						? matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('-'))
						: matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('.'))) + ".ids";
				FileUtilities.copyFile(idsFileName, this.resultFolderName + File.separator + projectName + ".ids");
				
				FileUtilities.saveMatrix(correlationMatrix2d, this.resultFolderName + File.separator + projectName + ".matrix");
			} catch (Exception e) {
				this.failedProjects.add(matrixFile);
				e.printStackTrace();
			}
			
			this.addAnalyzedProject();
		}
	}
	
	public static void main(String[] args) {
		String resultFolderName = "";
		CorrelationMatrixController controller = new CorrelationMatrixController(new File[] {});
		
		for (File matrixFile : new File("").listFiles(getMatrixFileFilter())) {
			try {
				String projectName = matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('-'));
				
				DoubleMatrix2D termDocumentMatrix = FileUtilities.readMatrix(matrixFile.getAbsolutePath());
				DoubleMatrix2D correlationMatrix2d = controller.buildCorrelationMatrix(termDocumentMatrix);
				
				String idsFileName = matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('-')) + ".ids";
				FileUtilities.copyFile(idsFileName, resultFolderName + File.separator + projectName + ".ids");
				
				FileUtilities.saveMatrix(correlationMatrix2d, resultFolderName + File.separator + projectName + ".matrix");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static FilenameFilter getMatrixFileFilter() {
		return new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith("-lsi.matrix");
		    }
		};
	}
}