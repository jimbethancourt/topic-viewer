package br.ufmg.aserg.topicviewer.control.indexing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.splabs.vocabulary.iR.IRPropertyKeys;
import org.splabs.vocabulary.iR.info.LSIInfo;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.control.correlation.clustering.HierarchicalClustering;
import br.ufmg.aserg.topicviewer.control.distribution.DistributionMapCalculator;
import br.ufmg.aserg.topicviewer.control.semantic.SemanticTopicsCalculator;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapGraphicPanel;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;
import cern.colt.matrix.DoubleMatrix1D;

public class SemanticClusteringController extends AbstractController {
	
	private File[] termDocMatrixFiles;
	private String lsiTermDocFolder;
	private String correlationFolder;
	
	private boolean useThreshold;
	private boolean getBestThreshold;
	private int numClusters;
	private double threshold;
	
	private HierarchicalClustering clusterer;
	
	private java.util.Properties props;
	
	public SemanticClusteringController(File[] termDocMatrixFiles, int lowRankValue, boolean useThreshold, boolean getBestThreshold, int numClusters, double threshold) {
		super();
		
		this.termDocMatrixFiles = termDocMatrixFiles;
		this.setNumStages(this.termDocMatrixFiles.length*3);
		
		this.lsiTermDocFolder = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT;
		this.correlationFolder = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.CORRELATION_MATRIX_OUTPUT;
		checkResultFolder(this.lsiTermDocFolder);
		checkResultFolder(this.correlationFolder);
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(IRPropertyKeys.LSI_LOW_RANK_VALUE, new Integer(lowRankValue).toString());
		Properties.setProperties(properties);
		
		this.useThreshold = useThreshold;
		this.getBestThreshold = getBestThreshold;
		this.numClusters = numClusters;
		this.threshold = threshold;
		this.props = Properties.getProperties();
	}
	
	private DoubleMatrix2D buildCorrelationMatrix(DoubleMatrix2D termDocumentMatrix) throws IOException {
		int numDocuments = termDocumentMatrix.columns();
		
		DoubleMatrix2D correlationMatrix = new DoubleMatrix2D(numDocuments, numDocuments);
		
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
		return denominator == 0 ? 1D : cosineSimilarity / denominator;
	}
	
	@Override
	public void run() {
		for (File termDocumentMatrixFile : this.termDocMatrixFiles) {
			String projectName = termDocumentMatrixFile.getName().substring(0, termDocumentMatrixFile.getName().lastIndexOf('.'));
			
			try {
			// -------------------------------------- LSI Indexing --------------------------------------
				this.setProgressMessage("Executing Latent Semantic Indexing for " + projectName + " project");
				
				String[] terms = FileUtilities.readTermIds(this.lsiTermDocFolder + File.separator + projectName + ".ids");
				String[] documents = FileUtilities.readDocumentIds(this.lsiTermDocFolder + File.separator + projectName + ".ids");
				DoubleMatrix2D termDocumentMatrix = new DoubleMatrix2D(this.lsiTermDocFolder + File.separator + projectName + ".matrix");
				
				LSIInfo lsiInfo = new LSIInfo(terms, documents, DoubleMatrix2D.copy(termDocumentMatrix), props);
				DoubleMatrix2D lsiTermDocumentMatrix = new DoubleMatrix2D(lsiInfo.getLsiTermDocumentMatrix());
				lsiTermDocumentMatrix.save(this.lsiTermDocFolder + File.separator + projectName + "-lsi.matrix");
				new DoubleMatrix2D(lsiInfo.getLsiTransformMatrix()).save(this.lsiTermDocFolder + File.separator + projectName + ".lsi");
				
				this.setProgressMessage("Vector Space reduced with LSI successfully for " + projectName + " project");
				this.addCompletedStage();
			
			// ------------------------------- Correlation Matrix indexing ------------------------------
				this.setProgressMessage("Calculating Correlation Matrix for " + projectName + " project");
				
				DoubleMatrix2D correlationMatrix = this.buildCorrelationMatrix(lsiTermDocumentMatrix);
				
				String idsFileName = termDocumentMatrixFile.getAbsolutePath().substring(0, termDocumentMatrixFile.getAbsolutePath().lastIndexOf('.')) + ".ids";
				FileUtilities.copyFile(idsFileName, this.correlationFolder + File.separator + projectName + ".ids");
				correlationMatrix.save(this.correlationFolder + File.separator + projectName + ".matrix");
			
				this.setProgressMessage("");
				this.addCompletedStage();
			
			// --------------------------------- Hierarchical Clustering --------------------------------
            	this.setProgressMessage("Executing Hierarchical Clustering for " + projectName + " project");
				
            	CorrelationMatrix matrix = new CorrelationMatrix(documents, correlationMatrix);
            	this.clusterer = new HierarchicalClustering(matrix, useThreshold, getBestThreshold, numClusters, threshold);
				FileUtilities.saveClustering(this.clusterer.getClusters(), documents, this.correlationFolder + File.separator + projectName + ".clusters");
				
				this.setProgressMessage("Generating Semantic Topics for " + projectName + " project");
				
				String[][] semanticTopics = SemanticTopicsCalculator.generateSemanticTopicsFromClasses(this.clusterer.getClusters(), terms, documents);
				FileUtilities.saveSemanticTopics(semanticTopics, this.correlationFolder + File.separator + projectName + ".topics");
				
	        	this.setProgressMessage("Semantic Topics generated for " + projectName + " project successfully");
	        	this.addCompletedStage();
				
			} catch (Exception e) {
				this.failedProjects.add(termDocumentMatrixFile);
				e.printStackTrace();
				
				this.addCompletedStage(); this.addCompletedStage(); this.addCompletedStage();
			}
		}
	}
}