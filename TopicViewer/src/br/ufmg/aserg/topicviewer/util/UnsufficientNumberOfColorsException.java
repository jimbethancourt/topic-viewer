package br.ufmg.aserg.topicviewer.util;

public class UnsufficientNumberOfColorsException extends Exception {
	
	private static final long serialVersionUID = 5100489853157295848L;

	public UnsufficientNumberOfColorsException(int maxColors) {
		super("Unsufficient Number of Colors. There are only " + maxColors + " colors to represent the semantic clusters for this project.");
	}

}