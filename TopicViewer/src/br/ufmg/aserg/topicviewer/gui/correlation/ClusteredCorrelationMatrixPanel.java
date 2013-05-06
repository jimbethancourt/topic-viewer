package br.ufmg.aserg.topicviewer.gui.correlation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.gui.draw.CorrelationRectangle;

public class ClusteredCorrelationMatrixPanel extends CorrelationMatrixPanel {

	private static final long serialVersionUID = -1410541470646368686L;
	
	private List<CorrelationRectangle> clusterRectangles;
	private List<ClusteredMatrixListener> listeners;
	
	public ClusteredCorrelationMatrixPanel(CorrelationMatrix matrix, int[][] clusters) {
		super(matrix);
		this.clusterRectangles = new LinkedList<CorrelationRectangle>();
		this.listeners = new LinkedList<ClusteredMatrixListener>();
		
		this.buildCorrelationMatrix(clusters);
		this.addMouseMotionListener(getMouseMotionListener());
	}
	
	private void buildCorrelationMatrix(int[][] clusters) {
		int index = matrixStrokeSize;
		
		for (int i = 0; i < clusters.length; i++) {
			int size = clusters[i].length * entitySize;
			CorrelationRectangle rectangle = new CorrelationRectangle(index+4, index+4, size, size, i);
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
	
	public void addListener(ClusteredMatrixListener listener) {
		this.listeners.add(listener);
	}
	
	private MouseMotionListener getMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				for (CorrelationRectangle rectangle : clusterRectangles)
					if (rectangle.contains(e.getPoint())) {
						for (ClusteredMatrixListener listener : listeners)
							listener.actionPerformed((int) rectangle.getValue());
						break;
					}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		};
	}
	
	public interface ClusteredMatrixListener {
		
		public void actionPerformed(Integer clusterIndex);
		
	}
}