package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Color;

public class ColorUtil {
	
	/*RGB
Grey values result when r = g = b, for the color (r, g, b)
*/

	// ir de 1 a 254
	public static Color generateGreyColor(double value, double minValue, double maxValue) { 
		
		
		return null;
	}
	
	public static Color generateNewGreyColor(Color color, boolean darker) {
		int colorCoordinate = color.getRed() + (darker ? -1 : 1);
		return new Color(colorCoordinate, colorCoordinate, colorCoordinate);
	}
}