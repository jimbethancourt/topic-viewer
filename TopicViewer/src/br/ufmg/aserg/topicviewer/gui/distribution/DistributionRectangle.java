package br.ufmg.aserg.topicviewer.gui.distribution;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import br.ufmg.aserg.topicviewer.gui.draw.ColorUtil;

public class DistributionRectangle extends Rectangle2D.Double {

	private static final long serialVersionUID = 1667564662506252314L;
	
	private String entityName;
	private String clusterTopics;
	private Color clusterColor;
	
	public DistributionRectangle(int x, int y, int width, int height, String className, int clusterIndex, String[] clusterTopics) {
		this.setRect(x, y, width, height);
		
		this.entityName = className;
		this.clusterTopics = getClusterTopics(clusterTopics);
		this.clusterColor = (clusterIndex != -1) ? ColorUtil.getDistributionColor(clusterIndex) : new Color(255, 255, 255);
	}
	
	public DistributionRectangle(int x, int y, int width, int height, String packageName) {
		this.setRect(x, y, width, height);
		
		this.entityName = packageName;
		this.clusterTopics = null;
		this.clusterColor = null;
	}
	
	private static String getClusterTopics(String[] clusterTopics) {
		String topics = "[";
		
		boolean first = true;
		for (String topic : clusterTopics) {
			topics += (first ? "" : ", ") + topic;
			first = false;
		}
		
		topics += "]";
		return topics;
	}
	
	@Override
	public String toString() {
		return this.getEntityName() + (this.clusterColor == null ? "" : ": [" + this.getClusterTopics() + "]");
	}
	
	public String getEntityName() {
		return this.entityName;
	}

	public String getClusterTopics() {
		return this.clusterTopics;
	}

	public Color getColor() {
		return this.clusterColor;
	}
}