package br.ufmg.aserg.topicviewer.control.correlation.clustering;

import java.io.File;
import java.io.FilenameFilter;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.control.distribution.DistributionMapCalculator;
import br.ufmg.aserg.topicviewer.control.semantic.SemanticTopicsCalculator;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapGraphicPanel;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

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
            	DoubleMatrix2D correlationMatrix = new DoubleMatrix2D(matrixFile.getAbsolutePath());
            	
            	CorrelationMatrix matrix = new CorrelationMatrix(documentIds, correlationMatrix);
            	double threshold = Double.parseDouble(Properties.getProperty(Properties.SIMILARITY_THRESHOLD));
				this.clusterer = new HierarchicalClustering(projectName, matrix, documentIds, this.numClusters, threshold);
				
				FileUtilities.saveClustering(this.clusterer.getClusters(), documentIds, this.resultFolderName + File.separator + projectName + ".clusters");
//				FileUtilities.saveMapping(this.clusterer.getIndexMapping(), this.resultFolderName + File.separator + projectName + ".mapping");
//				this.clusterer.getClusteredMatrix().save(this.resultFolderName + File.separator + projectName + "-clustered.matrix");
//				this.clusterer.getClusteredWithLinksMatrix().save(this.resultFolderName + File.separator + projectName + "-clusteredlinked.matrix");
				
				String[] termIds = FileUtilities.readTermIds(idsFileName);
				String[][] semanticTopics = SemanticTopicsCalculator.generateSemanticTopicsFromClasses(this.clusterer.getClusters(), termIds, documentIds);
				FileUtilities.saveSemanticTopics(semanticTopics, this.resultFolderName + File.separator + projectName + ".topics");
				
				int[][] clusters = this.clusterer.getClusters();
				String lsiMatrixFile = matrixFile.getAbsolutePath().replace(Properties.CORRELATION_MATRIX_OUTPUT, Properties.TERM_DOC_MATRIX_OUTPUT);
				lsiMatrixFile.replace(".matrix", "-lsi.matrix");
				DoubleMatrix2D lsiTermDocMatrix = new DoubleMatrix2D(lsiMatrixFile);
				ClusteringEvaluationController.calculateQualityMetrics(lsiTermDocMatrix, clusters);
				
				DistributionMap distributionMap = DistributionMapCalculator.generateDistributionMap(this.resultFolderName + File.separator + projectName, documentIds, clusters);
	        	new DistributionMapGraphicPanel(distributionMap, semanticTopics);
	        	
			} catch (Exception e) {
				this.failedProjects.add(matrixFile);
				e.printStackTrace();
			}
			
			this.addAnalyzedProject();
		}
	}
	
	public static void main(String[] args) {
		// correlation matrix folder
		String resultFolderName = "C:\\Users\\admin\\Dropbox\\experiments\\correlation";
		
		final int numClusters = 9;
		HierarchicalClustering clusterer;
		
		for (File matrixFile : new File("C:\\Users\\admin\\Documents\\mestrado\\correlation2").listFiles(getMatrixFileFilter())) {
			try {
				String projectName = matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('.'));
				String idsFileName = matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('.')) + ".ids";
            	
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	DoubleMatrix2D correlationMatrix = new DoubleMatrix2D(matrixFile.getAbsolutePath());
            	
            	CorrelationMatrix matrix = new CorrelationMatrix(documentIds, correlationMatrix);
				clusterer = new HierarchicalClustering(projectName, matrix, documentIds, numClusters, 0.7D);
				
				FileUtilities.saveClustering(clusterer.getClusters(), documentIds, resultFolderName + File.separator + projectName + ".clusters");
				FileUtilities.saveMapping(clusterer.getIndexMapping(), resultFolderName + File.separator + projectName + ".mapping");
				clusterer.getClusteredMatrix().save(resultFolderName + File.separator + projectName + "-clustered.matrix");
				
				String[] termIds = FileUtilities.readTermIds(idsFileName);
				String lsiTermDocFileName = "C:\\Users\\admin\\Documents\\mestrado\\lsi" + File.separator + projectName + "-lsi.matrix";
				String lsiTransformFileName = "C:\\Users\\admin\\Documents\\mestrado\\lsi" + File.separator + projectName + ".lsi";
				
				DoubleMatrix2D lsiTermDocMatrix = new DoubleMatrix2D(lsiTermDocFileName);
				DoubleMatrix2D lsiTransformMatrix = new DoubleMatrix2D(lsiTransformFileName);
				
				String[][] semanticTopics = SemanticTopicsCalculator.generateSemanticTopics(clusterer.getClusters(), lsiTermDocMatrix, lsiTransformMatrix, termIds);
				FileUtilities.saveSemanticTopics(semanticTopics, resultFolderName + File.separator + projectName + ".topics");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static FilenameFilter getMatrixFileFilter() {
		return new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".matrix");
		    }
		};
	}
}