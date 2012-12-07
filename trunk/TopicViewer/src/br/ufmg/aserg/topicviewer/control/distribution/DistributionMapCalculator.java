package br.ufmg.aserg.topicviewer.control.distribution;

import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;

public class DistributionMapCalculator {
	
	public static DistributionMap generateDistributionMap(String[] documentIds, int[][] clusters) {
		
		DistributionMap distributionMap = new DistributionMap();
		for (int i = 0; i < clusters.length; i++)
			for (int documentId : clusters[i]) {
				String document = documentIds[documentId];
				String packageName = document.substring(document.lastIndexOf(':')+1, document.lastIndexOf('.'));
				String className = document.substring(document.lastIndexOf('.')+1);
				
				distributionMap.put(packageName, className, i);
			}
		
		distributionMap.organize();
		return distributionMap;
	}
}