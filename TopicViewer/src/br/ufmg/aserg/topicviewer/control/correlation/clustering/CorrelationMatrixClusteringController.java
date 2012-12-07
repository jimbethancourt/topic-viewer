package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.io.File;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.control.semantic.SemanticTopicsCalculator;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;
import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrixClusteringController extends AbstractController {
	
	private File[] matrixFiles;
	private Integer numClusters;
	
	private HierarchicalClustering clusterer;
	
	public CorrelationMatrixClusteringController(File[] correlationMatrixFiles, int numClusters) {
		this.matrixFiles = correlationMatrixFiles;
		this.numClusters = numClusters;
		
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.CORRELATION_MATRIX_OUTPUT;
		checkResultFolder();
		
		this.setAllProjectCount(this.matrixFiles.length);
	}

	@Override
	public void run() {
		for (File matrixFile : this.matrixFiles) {
			try {
				String projectName = matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('.'));
				String idsFileName = matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('.')) + ".ids";
            	
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	DoubleMatrix2D correlationMatrix = FileUtilities.readMatrix(matrixFile.getAbsolutePath());
            	
            	CorrelationMatrix matrix = new CorrelationMatrix(documentIds, correlationMatrix);
				this.clusterer = new HierarchicalClustering(matrix, this.numClusters);
				
				FileUtilities.saveClustering(this.clusterer.getClusters(), this.resultFolderName + File.separator + projectName + ".clusters");
				FileUtilities.saveMapping(this.clusterer.getIndexMapping(), this.resultFolderName + File.separator + projectName + ".mapping");
				FileUtilities.saveMatrix(this.clusterer.getClusteredMatrix(), this.resultFolderName + File.separator + projectName + "-clustered.matrix");
				FileUtilities.saveMatrix(this.clusterer.getClusteredWithLinksMatrix(), this.resultFolderName + File.separator + projectName + "-clusteredlinked.matrix");
				
				String[] termIds = FileUtilities.readTermIds(idsFileName);
				String lsiTermDocFileName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT 
						+ File.separator + projectName + "-lsi.matrix";
				String lsiTransformFileName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT 
						+ File.separator + projectName + ".lsi";
				
				DoubleMatrix2D lsiTermDocMatrix = FileUtilities.readMatrix(lsiTermDocFileName);
				DoubleMatrix2D lsiTransformMatrix = FileUtilities.readMatrix(lsiTransformFileName);
				
				String[][] semanticTopics = SemanticTopicsCalculator.generateSemanticTopics(this.clusterer.getClusters(), lsiTermDocMatrix, lsiTransformMatrix, termIds);
				FileUtilities.saveSemanticTopics(semanticTopics, this.resultFolderName + File.separator + projectName + ".topics");
			} catch (Exception e) {
				this.failedProjects.add(matrixFile);
				e.printStackTrace();
			}
			
			this.addAnalyzedProject();
		}
	}
}