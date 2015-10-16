package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufmg.aserg.topicviewer.control.distribution.DistributionMap;

public class TopicConcentration extends AbstractSemanticTopicMetric {
	
	public TopicConcentration(DistributionMap distributionMap, int numClusters) {
		super(distributionMap, numClusters);
	}

	private double calculate(String packageName) {
		
		Set<Integer> packageTopics = new HashSet<Integer>();
		double[] topicFrequency = new double[this.numClusters];
		double numClasses = 0D;
		
		List<String> classes = this.distributionMap.getClasses(packageName);
		for (String className : classes) {
			int clusterIndex = this.distributionMap.getCluster(className);
			
			if (clusterIndex != -1) {
				numClasses++;
				packageTopics.add(clusterIndex);
				topicFrequency[clusterIndex]++;
			}
		}
		
		Arrays.sort(topicFrequency);
		double concentration = 0D;
		
		int index = -1;
//		double lastFrequency = 0;
		double strengthFactor = 0.5;
		
		for (int i = topicFrequency.length-1; i >= 0; i--) {
			index++;
			if (numClasses > 0) 
				concentration += (topicFrequency[i]/numClasses) * Math.pow(strengthFactor, index);
		}
		
		return concentration;
	}
	
	public Map<String, Double> calculate() {
		Map<String, Double> metricMapping = new HashMap<String, Double>();
		for (String packageName : this.distributionMap.getPackages())
			metricMapping.put(packageName, calculate(packageName));
		return metricMapping;
	}
}