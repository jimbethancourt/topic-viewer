package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Color;

public class ColorUtil {
	
	public static Color generateGreyColor(double value, double minValue, double maxValue) { 
		int colorValue = (int) (((value + 1) / 2) * 245 + 10F);
		colorValue = 255 - colorValue;
		return new Color(colorValue, colorValue, colorValue);
	}
	
}