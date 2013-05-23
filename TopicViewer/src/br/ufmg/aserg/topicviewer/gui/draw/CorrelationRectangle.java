package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class CorrelationRectangle extends Rectangle2D.Double {

	private static final long serialVersionUID = 1667564662506252314L;
	private static final char stringSeparator = ':';
	
	private String xEntityId;
	private String yEntityId;
	private double value;
	
	private Color color;
	
	public CorrelationRectangle(int x, int y, int width, int height, String xEntityId, String yEntityId, double value) {
		this.setRect(x, y, width, height);
		
		this.xEntityId = xEntityId.substring(xEntityId.lastIndexOf(stringSeparator)+1);
		this.yEntityId = yEntityId.substring(yEntityId.lastIndexOf(stringSeparator)+1);
		this.value = value;
		this.color = ColorUtil.generateGreyColor(value);
	}
	
	public CorrelationRectangle(int x, int y, int width, int height, double value) {
		this.setRect(x, y, width, height);
		
		this.xEntityId = this.yEntityId = "";
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "[" + this.getXEntityId() + ", " + this.getYEntityId() + "] = " + this.getValue();
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