package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.io.File;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrixClusteringController extends AbstractController {
	
	private File[] matrixFiles;
	private Integer numClusters;
	
	private HierarchicalClustering clusterer;
	
	public CorrelationMatrixClusteringController(File[] correlationMatrixFiles, int numClusters) {
		this.matrixFiles = correlationMatrixFiles;
		this.numClusters = numClusters;
	}

	@Override
	public void run() {
		for (File matrixFile : this.matrixFiles) {
			try {
				String idsFileName = matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('.')) + ".ids";
            	
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	DoubleMatrix2D correlationMatrix = FileUtilities.readMatrix(matrixFile.getAbsolutePath());
            	
            	CorrelationMatrix matrix = new CorrelationMatrix(documentIds, correlationMatrix);
				
				this.clusterer = new HierarchicalClustering(matrix, this.numClusters);
				
				// TODO adicionar jtext na interface
//				private DoubleMatrix2D clusteredMatrix;
//				private DoubleMatrix2D clusteredWithLinksMatrix;
//				private Map<Integer, Integer> indexMapping;
//				private int[][] clusters;
				
				
			} catch (Exception e) {
				this.failedProjects.add(matrixFile);
				e.printStackTrace();
			}
			
			this.addAnalyzedProject();
		}
	}
}