package br.ufmg.aserg.topicviewer.control.measurement.metrics;

import java.util.HashMap;
import java.util.Map;

import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;

public class TopicFocus extends AbstractSemanticTopicMetric {
	
	public TopicFocus(DistributionMap distributionMap, String[][] semanticTopics) {
		super(distributionMap, semanticTopics);
	}

	private double calculate(int topicIndex) {
		double focus = 0D;
		for (String packageName : this.distributionMap.getPackages())
			focus += this.calculateTopicTouch(topicIndex, packageName) * this.calculatePackageTouch(packageName, topicIndex);
		return focus;
	}

	public Map<String, Double> calculate() {
		Map<String, Double> metricMapping = new HashMap<String, Double>();
		for (int topicIndex = 0; topicIndex < this.numTopics; topicIndex++)
			metricMapping.put(String.valueOf(topicIndex), calculate(topicIndex));
		return metricMapping;
	}
}