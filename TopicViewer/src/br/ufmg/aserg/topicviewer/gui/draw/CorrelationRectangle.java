package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Color;
import java.awt.Rectangle;

public class CorrelationRectangle extends Rectangle {

	private static final long serialVersionUID = 1667564662506252314L;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final int MIN_VALUE = -1;
	private static final int MAX_VALUE = 1;
	
	private String xEntityId;
	private String yEntityId;
	private double value;
	
	private Color color;
	
	public CorrelationRectangle(int x, int y, int width, int height, String xEntityId, String yEntityId, double value) {
		this.setBounds(x, y, width, height);
		
		this.xEntityId = xEntityId;
		this.yEntityId = yEntityId;
		this.value = value;
		this.color = ColorUtil.generateGreyColor(value, MIN_VALUE, MAX_VALUE);
	}
	
	private void select(boolean select) {
		this.color = ColorUtil.generateNewGreyColor(this.color, select);
	}
	
	@Override
	public String toString() {
		return this.getXEntityId() + LINE_SEPARATOR 
				+ this.getYEntityId() + LINE_SEPARATOR 
				+ this.getValue();
	}

	public String getXEntityId() {
		return xEntityId;
	}

	public String getYEntityId() {
		return yEntityId;
	}

	public double getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}
}