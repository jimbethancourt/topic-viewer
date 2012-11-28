package br.ufmg.aserg.topicviewer.gui.correlation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.gui.draw.CorrelationRectangle;

public class CorrelationMatrixGraphicPanel extends JPanel {
	
	private static final long serialVersionUID = -6391510670978885799L;

	protected CorrelationMatrix correlationMatrix;
	private Rectangle2D.Double matrixExternalView;
	
	protected static final Integer matrixStrokeSize = 5;
	protected static final Integer entitySize = 8;
	
	private List<CorrelationRectangle> rectangles;
	
	public CorrelationMatrixGraphicPanel(CorrelationMatrix matrix) {
		super();
		
		this.correlationMatrix = matrix;
		this.rectangles = new LinkedList<CorrelationRectangle>();
		
		int bounds = (matrixStrokeSize * 2) + entitySize * (this.correlationMatrix.getNumEntities());
		this.matrixExternalView = new Rectangle2D.Double(5, 5, bounds, bounds);
		
		this.buildCorrelationMatrix();
		this.addMouseMotionListener(getMouseMotionListener());
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(bounds, bounds));
		this.setPreferredSize(new Dimension(bounds, bounds));
	}
	
	private void buildCorrelationMatrix() {
		for (int i = 0; i < this.correlationMatrix.getNumEntities(); i++)
			for (int j = 0; j < this.correlationMatrix.getNumEntities(); j++) {
				int x = matrixStrokeSize + (i * entitySize) + 5;
				int y = matrixStrokeSize + (j * entitySize) + 5;
				String xId = this.correlationMatrix.getIdAt(i);
				String yId = this.correlationMatrix.getIdAt(j);
				Double correlation = this.correlationMatrix.getValueAt(i, j);
				
				CorrelationRectangle rectangle = new CorrelationRectangle(x, y, entitySize, entitySize, xId, yId, correlation);
				this.rectangles.add(rectangle);
			}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D graphics = (Graphics2D) g;
		
		// paint matrix border
		graphics.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graphics.fill(this.matrixExternalView);
		
		// paint rectangles
		graphics.setStroke(new BasicStroke(0));
		for (CorrelationRectangle rectangle : this.rectangles) {
			graphics.setColor(rectangle.getColor());
			graphics.fill(rectangle);
		}
	}
	
	private MouseMotionListener getMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				for (CorrelationRectangle rectangle : rectangles)
					if (rectangle.contains(e.getPoint())) {
						setToolTipText(rectangle.toString());
						ToolTipManager.sharedInstance().mouseMoved(e);
						return;
					}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		};
	}
}