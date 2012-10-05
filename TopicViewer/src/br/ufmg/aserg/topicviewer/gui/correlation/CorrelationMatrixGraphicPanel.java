package br.ufmg.aserg.topicviewer.gui.correlation;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;

public class CorrelationMatrixGraphicPanel extends JPanel {
	
	private static final long serialVersionUID = -6391510670978885799L;

	private CorrelationMatrix correlationMatrix;
	private Rectangle2D.Double matrixView;
	
	private static final Integer initCoordinate = 10;
	private static final Integer matrixStrokeSize = 5;
	private static final Integer entitySize = 8;
	
	public CorrelationMatrixGraphicPanel(CorrelationMatrix matrix) {
		super();
		
		this.correlationMatrix = matrix;
		
		int bounds = (matrixStrokeSize * 2) + entitySize * (matrix.getNumEntities());
		this.matrixView = new Rectangle2D.Double(initCoordinate, initCoordinate, bounds, bounds);
		this.buildCorrelationMatrix();
	}
	
	private void buildCorrelationMatrix() {
		
	}
	
	
			  private Rectangle2D.Double square =
			    new Rectangle2D.Double(10, 10, 350, 350);

			  public void paintComponent(Graphics g) {
			    clear(g);
			    Graphics2D g2d = (Graphics2D)g;
			    g2d.setStroke(new BasicStroke(5));
			    g2d.draw(square);
			  }

			  protected void clear(Graphics g) {
			    super.paintComponent(g);
			  }


}
