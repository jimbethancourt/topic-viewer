package br.ufmg.aserg.topicviewer.gui.correlation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.gui.draw.CorrelationRectangle;
import br.ufmg.aserg.topicviewer.util.Properties;

public class CorrelationMatrixPanel extends JPanel {
	
	private static final long serialVersionUID = -6391510670978885799L;

	protected CorrelationMatrix correlationMatrix;
	private Rectangle2D.Double matrixExternalView;
	
	protected static final Integer matrixStrokeSize = 5;
	protected static final Integer entitySize = 8;
	
	private int bound;
	
	private List<CorrelationRectangle> rectangles;
	
	private JMenuItem saveImageMenuItem;
	private JPopupMenu rightClickPopupMenu;
	
	public CorrelationMatrixPanel(CorrelationMatrix matrix) {
		super();
		
		this.correlationMatrix = matrix;
		this.rectangles = new LinkedList<CorrelationRectangle>();
		
		this.bound = (matrixStrokeSize * 2) + entitySize * (this.correlationMatrix.getNumEntities());
		this.matrixExternalView = new Rectangle2D.Double(5, 5, bound, bound);
		
		this.buildCorrelationMatrix();
		this.addRightClickListener();
		this.addMouseMotionListener(getMouseMotionListener());
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(bound, bound));
		this.setPreferredSize(new Dimension(bound, bound));
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
	
	private void addRightClickListener() {
		this.saveImageMenuItem = new JMenuItem("Save Image...");
		this.rightClickPopupMenu = new JPopupMenu();
		
		this.rightClickPopupMenu.add(saveImageMenuItem);
        this.setComponentPopupMenu(rightClickPopupMenu);
        
        this.saveImageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					saveImageActionPerformed();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error Saving File:\nCause:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
            }
        });
	}
	
	private void saveImageActionPerformed() throws IOException {
		JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.WORKSPACE)));
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Image Files (.png)";
            }
        });
        
		if (chooser.showSaveDialog(this) != JFileChooser.CANCEL_OPTION) {
			this.saveImage(chooser.getSelectedFile());
		}
	}
	
	public void saveImage(File file) throws IOException {
		BufferedImage image = new BufferedImage(this.bound, this.bound, BufferedImage.TYPE_INT_RGB);
		this.paintComponent(image.getGraphics());

	    ImageIO.write(image, "PNG", new File(file.getAbsolutePath() + ".png"));
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