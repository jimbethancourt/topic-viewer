package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;

public class TopicConcentration extends AbstractSemanticTopicMetric {
	
	public TopicConcentration(DistributionMap distributionMap, String[][] semanticTopics) {
		super(distributionMap, semanticTopics);
	}

	private double calculate(String packageName) {
		Set<Integer> packageTopics = new HashSet<Integer>();
		double[] topicFrequency = new double[this.numTopics];
		
		List<String> classes = this.distributionMap.getClasses(packageName);
		for (String className : classes) {
			int clusterIndex = this.distributionMap.getCluster(className);
			
			if (clusterIndex != -1) {
				packageTopics.add(clusterIndex);
				topicFrequency[clusterIndex]++;
			}
		}
		
		Arrays.sort(topicFrequency);
		double concentration = 0D;
		double clusterSize = classes.size();
		double strengthFactor = 1D;
		for (int i = topicFrequency.length-1; i >= 0; i--) {
			concentration += (topicFrequency[i]/clusterSize) * strengthFactor;
			strengthFactor *= 0.5;
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