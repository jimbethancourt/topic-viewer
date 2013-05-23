package br.ufmg.aserg.topicviewer.gui.draw;

import java.awt.Color;

import br.ufmg.aserg.topicviewer.util.UnsufficientNumberOfColorsException;

public class ColorUtil {
	
	public static Color generateGreyColor(double value) { 
		int colorValue = (int) (((value + 1D) / 2D) * 245D + 10D);
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
		new Color(75, 0, 130), // indigo
		new Color(255, 218, 185), // peach puff
		new Color(230, 230, 250), // lavender
		new Color(47, 79, 79), // dark slate gray
		new Color(72, 61, 139), // dark slate blue
		new Color(50, 205, 50), // lime green
		
		new Color(189, 183, 107), // dark khaki
		new Color(184, 134, 11), // dark goldenrod
		new Color(244, 164, 96), // sandy brown
		new Color(210, 105, 30), // chocolate
		new Color(250, 128, 114), // salmon
		new Color(255, 99, 71), // tomato
		new Color(255, 127, 80), // coral
		new Color(255, 105, 180), // hot pink
		new Color(219, 112, 147), // pale violet rad
		new Color(255, 0, 255), // magenta
		
		new Color(176, 48, 96), // maroon
		new Color(221, 160, 221), // plum
		new Color(72, 118, 255), // royal blue 1
		new Color(102, 205, 0), // chartreuse 3
		new Color(255, 174, 185), // light pink 1
		new Color(112, 147, 219), // dark turquoise
		new Color(165, 128, 100), // medium wood
		new Color(181, 165, 66), // light copper
		new Color(192, 217, 217), // bright blue
		new Color(255, 48, 48), // firebrick1
		
		new Color(238, 216, 174), // wheat2
		new Color(102, 205, 170), // medium aquamarine
		new Color(188, 143, 143), // rosy brown
		new Color(205, 205, 193), // ivory3
		new Color(176, 226, 255), // light blue sky 1
		new Color(192, 255, 62), // olivedrab1
		new Color(139, 115, 85), // burlywood4
		new Color(171, 130, 255), // medium purple1
		new Color(238, 210, 238), // thistle2
		new Color(139, 136, 120) // cornsilk4
	};
	
	public static Color getDistributionColor(int colorIndex) throws UnsufficientNumberOfColorsException {
		if (colorIndex >= distributionColors.length)
			throw new UnsufficientNumberOfColorsException(distributionColors.length);
		
		Color color = distributionColors[colorIndex];
		return color;
	}
}