package br.ufmg.aserg.topicviewer.control.measurement;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.ConceptualCohesionOfPackages;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.ConceptualCouplingBetweenPackages;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.ConceptualCouplingOfPackages;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.ConceptualLackOfCohesionBetweenClasses;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.LackOfConceptualSimilarityBetweenClasses;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class ConceptualMetricsCalculatorController extends AbstractController {
	
	private File[] matrixFiles;
	
	public ConceptualMetricsCalculatorController(File[] matrixFiles) {
		super();
		
		this.matrixFiles = matrixFiles;
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.METRICS_OUTPUT;
		this.checkResultFolder();
		
		this.setAllProjectCount(matrixFiles.length);
	}
	
	@Override
	public void run() {
		for (File matrixFile : this.matrixFiles) {
			try {
				String projectName = (matrixFile.getName().contains("-lsi") || matrixFile.getName().contains("-cluster")) 
						? matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('-'))
						: matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('.'));
				String idsFileName = ((matrixFile.getAbsolutePath().contains("-lsi") || matrixFile.getName().contains("-cluster")) 
						? matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('-'))
						: matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('.'))) + ".ids";
				
				DoubleMatrix2D termDocumentMatrix = new DoubleMatrix2D(matrixFile.getAbsolutePath());
				String[] documendIds = FileUtilities.readDocumentIds(idsFileName);
				
				// Cohesion Metrics
				ConceptualLackOfCohesionBetweenClasses CLCOC5 = new ConceptualLackOfCohesionBetweenClasses(termDocumentMatrix, documendIds);
				ConceptualCohesionOfPackages CCP = new ConceptualCohesionOfPackages(CLCOC5);
				LackOfConceptualSimilarityBetweenClasses LCSC = new LackOfConceptualSimilarityBetweenClasses(CLCOC5);
				
				// Coupling Metrics
				ConceptualCouplingBetweenPackages CCBP = new ConceptualCouplingBetweenPackages(CLCOC5);
				ConceptualCouplingOfPackages CCoP = new ConceptualCouplingOfPackages(CLCOC5);
				
				List<Map<String, Double>> results = new LinkedList<Map<String, Double>>();
				results.add(CLCOC5.calculate());
				results.add(CCP.calculate());
				results.add(LCSC.calculate());
				results.add(CCBP.calculate());
				results.add(CCoP.calculate());
				
				FileUtilities.saveMetricResults(new String[]{"CLCOC5", "CCP", "LCSC", "CCBP", "CCoP"}, results, this.resultFolderName + File.separator + projectName + ".results");
			} catch (Exception e) {
				this.failedProjects.add(matrixFile);
				e.printStackTrace();
			}
			
			this.addAnalyzedProject();
		}
	}
	
	public static void main(String[] args) {
		String resultFolderName = "G:\\semantic";
		
		for (File matrixFile : new File("G:\\lsi").listFiles(getMatrixFileFilter())) {
			try {
				String projectName = matrixFile.getName().substring(0, matrixFile.getName().lastIndexOf('-'));
				String idsFileName = matrixFile.getAbsolutePath().substring(0, matrixFile.getAbsolutePath().lastIndexOf('-')) + ".ids";
				
				DoubleMatrix2D termDocumentMatrix = new DoubleMatrix2D(matrixFile.getAbsolutePath());
				String[] documendIds = FileUtilities.readDocumentIds(idsFileName);
				
				// Cohesion Metrics
				ConceptualLackOfCohesionBetweenClasses CLCOC5 = new ConceptualLackOfCohesionBetweenClasses(termDocumentMatrix, documendIds);
				ConceptualCohesionOfPackages CCP = new ConceptualCohesionOfPackages(CLCOC5);
				LackOfConceptualSimilarityBetweenClasses LCSC = new LackOfConceptualSimilarityBetweenClasses(CLCOC5);
				
				// Coupling Metrics
				ConceptualCouplingBetweenPackages CCBP = new ConceptualCouplingBetweenPackages(CLCOC5);
				ConceptualCouplingOfPackages CCoP = new ConceptualCouplingOfPackages(CLCOC5);
				
				List<Map<String, Double>> results = new LinkedList<Map<String, Double>>();
				results.add(CLCOC5.calculate());
				results.add(CCP.calculate());
				results.add(LCSC.calculate());
				results.add(CCBP.calculate());
				results.add(CCoP.calculate());
				
				FileUtilities.saveMetricResults(new String[]{"CLCOC5", "CCP", "LCSC", "CCBP", "CCoP"}, results, resultFolderName + File.separator + projectName + ".results");
				System.out.println(projectName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static FilenameFilter getMatrixFileFilter() {
		return new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith("-lsi.matrix") && name.contains("FindBugs");
		    }
		};
	}
}