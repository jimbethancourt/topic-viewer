package br.ufmg.aserg.topicviewer.gui.correlation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;

public class ClusteredCorrelationMatrixGraphicPanel extends CorrelationMatrixGraphicPanel {

	private static final long serialVersionUID = -1410541470646368686L;
	
	private List<Rectangle2D> clusterRectangles;
	
	public ClusteredCorrelationMatrixGraphicPanel(CorrelationMatrix matrix, int[][] clusters) {
		super(matrix);
		this.clusterRectangles = new LinkedList<Rectangle2D>();
		
		this.buildCorrelationMatrix(clusters);
	}
	
	private void buildCorrelationMatrix(int[][] clusters) {
		int index = matrixStrokeSize;
		
		for (int[] cluster : clusters) {
			int size = cluster.length * entitySize;
			Rectangle2D rectangle = new Rectangle2D.Double(index+4, index+4, size, size);
			index += size;
			this.clusterRectangles.add(rectangle);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;
		
		graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graphics.setColor(new Color(210,210,210));
		
		// paint rectangles
		for (Rectangle2D rectangle : this.clusterRectangles)
			graphics.draw(rectangle);
	}
}