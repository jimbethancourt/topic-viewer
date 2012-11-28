package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Color;

public class ColorUtil {
	
	public static Color generateGreyColor(double value, double minValue, double maxValue) { 
		int colorValue = (int) (((value + 1) / 2) * 245 + 10F);
		colorValue = 255 - colorValue;
		return new Color(colorValue, colorValue, colorValue);
	}
	
	private static Color[] distributionColors = {
		new Color(238, 0, 0), // red
		new Color(0, 0, 238), // blue
		new Color(0, 205, 205), // cyan
		new Color(0, 205, 0), // light green
		new Color(238, 201, 0), // gold yellow
		new Color(238, 154, 0), // orange
		new Color(125, 38, 205), // purple
		new Color(238, 121, 159), // pink
		new Color(0, 100, 0), // dark green
		new Color(139, 69, 19), // brown chocolate
		new Color(255, 255, 0), // yellow
		new Color(105, 139, 34), // olive
		new Color(238, 99, 99), // indian red
		new Color(0, 134, 139), // turquoise
		new Color(75, 0, 130) // indigo
	};
	
	public static Color getDistributionColor(int colorIndex) {
		Color color = distributionColors[colorIndex];
		return color;
	}
}