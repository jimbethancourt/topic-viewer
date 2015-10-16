package br.ufmg.aserg.topicviewer.control.distribution;

import java.util.Map;

import br.ufmg.aserg.topicviewer.control.measurement.metrics.ConceptualCohesionOfPackages;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.TopicFocus;
import br.ufmg.aserg.topicviewer.control.measurement.metrics.TopicSpread;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;

public class DistributionMapCalculator {
	
	public static DistributionMap generateDistributionMap(String projectName, DoubleMatrix2D termDocumentMatrix, String[] documentIds, int[][] clusters, boolean sortByName) {
		
		Map<String, Double> ccp = new ConceptualCohesionOfPackages(termDocumentMatrix, documentIds).calculate();
		
		DistributionMap distributionMap = new DistributionMap(projectName);
		for (int i = 0; i < clusters.length; i++)
			for (int documentId : clusters[i]) {
				String document = documentIds[documentId];
				String packageName = document.substring(document.lastIndexOf(':')+1, document.lastIndexOf('.'));
				String className = document.substring(document.lastIndexOf('.')+1);
				
				distributionMap.put(packageName, className, i, ccp.get(packageName));
			}
		
		distributionMap = addSemanticClustersMetrics(distributionMap, clusters.length);
		distributionMap.organize(sortByName);
		return distributionMap;
	}
	
	public static DistributionMap addSemanticClustersMetrics(DistributionMap distributionMap, int numClusters) {
		
		Map<String, Double> spread = new TopicSpread(distributionMap, numClusters).calculate();
		Map<String, Double> focus = new TopicFocus(distributionMap, numClusters).calculate();
		
    	for (int i = 0; i < numClusters; i++)
			distributionMap.put(i, spread.get(String.valueOf(i)), focus.get(String.valueOf(i)));
		
		return distributionMap;
	}
}