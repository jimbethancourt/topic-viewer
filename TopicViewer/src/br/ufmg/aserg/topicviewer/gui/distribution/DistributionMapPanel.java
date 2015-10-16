package br.ufmg.aserg.topicviewer.gui.distribution;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
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

import br.ufmg.aserg.topicviewer.control.distribution.DistributionMap;
import br.ufmg.aserg.topicviewer.util.Properties;
import br.ufmg.aserg.topicviewer.util.UnsufficientNumberOfColorsException;

public class DistributionMapPanel extends JPanel {

	private static final long serialVersionUID = 6060506974958934930L;
	
	private static final Integer packageStroke = 3;
	private static final Integer packageSpace = 8;
	private static final Integer classStroke = 2;
	private static final Integer classSpace = 3;
	private static final Integer classSize = 17;
	
	private static final Integer maxPackages = 10;
	private static final Integer maxClasses = 5;
	
	private DistributionMap distributionMap;
	private String[][] semanticTopics;
	
	private int xBound = 0;
	private int yBound = 0;
	
	private Rectangle2D.Double externalView;
	private List<DistributionRectangle> packageRectangles;
	private List<DistributionRectangle> pckgLabelRectangles;
	private List<DistributionRectangle> classRectangles;
	private List<DistributionRectangle> classLabelRectangles;
	private List<DistributionRectangle> labelRectangles;
	
	private JMenuItem saveImageMenuItem;
	private JPopupMenu rightClickPopupMenu;
	
	public DistributionMapPanel(DistributionMap distributionMap, String[][] semanticTopics) throws UnsufficientNumberOfColorsException {
		super();
		
		this.distributionMap = distributionMap;
		this.semanticTopics = semanticTopics;
		
		this.packageRectangles = new LinkedList<DistributionRectangle>();
		this.pckgLabelRectangles = new LinkedList<DistributionRectangle>();
		this.classRectangles = new LinkedList<DistributionRectangle>();
		this.classLabelRectangles = new LinkedList<DistributionRectangle>();
		this.labelRectangles = new LinkedList<DistributionRectangle>();
		
		this.buildDistributionMap();
		this.addRightClickListener();
		this.addMouseMotionListener(getMouseMotionListener());
		this.setLayout(new BorderLayout());
		
		this.externalView = new Rectangle2D.Double(5, 5, this.xBound, this.yBound);
		this.setSize(new Dimension(this.xBound, this.yBound));
		this.setPreferredSize(new Dimension(this.xBound, this.yBound));
	}
	
	private void buildDistributionMap() throws UnsufficientNumberOfColorsException {
		
		int packageIndex = 0;
		int packageX = packageSpace;
		int packageY = packageSpace;
		int maxPackageHeight = 0;
		
		for (String packageName : this.distributionMap.getPackages()) {
			List<String> classes = this.distributionMap.getClasses(packageName);
			int numClasses = classes.size();
			
//			int numColumns = Math.min(maxClasses, numClasses);
			int numColumns = maxClasses;
			int packageWidth = 2*packageStroke + 2*classSpace 
					+ numColumns*(2*classStroke + classSize) 
					+ (numColumns-1)*classSpace;
			int numLines = (int) Math.ceil((double) numClasses / maxClasses);
			int packageHeight = 2*packageStroke + 2*classSpace 
					+ numLines*(2*classStroke + classSize)
					+ (numLines-1)*classSpace;
			maxPackageHeight = Math.max(packageHeight, maxPackageHeight);
			
			String packageInfo = packageName + " [CCP=" + this.distributionMap.getCCP(packageName) + "]";
			
			this.packageRectangles.add(new DistributionRectangle(packageX, packageY, packageWidth, packageHeight, packageInfo));
			this.buildDistributionMap(classes, packageX, packageY);
			
			this.pckgLabelRectangles.add(new DistributionRectangle(packageX, packageY + packageHeight + packageStroke + 2*packageSpace, classSize, classSize, packageName));
			
			packageX += packageWidth + packageSpace;
			this.xBound = Math.max(this.xBound, packageX);
			this.yBound = Math.max(this.yBound, packageY + maxPackageHeight + packageSpace + classSize + packageSpace);
			
			packageIndex++;
			if (packageIndex == maxPackages) {
				packageX = packageSpace;
				packageY += maxPackageHeight + packageSpace + classSize + packageSpace;
				maxPackageHeight = 0;
				packageIndex = 0;
			}
		}
		
		// build labels
		int labelX = packageSpace;
		int labelY = this.yBound + packageSpace;
		for (int i = 0; i < this.semanticTopics.length; i++) {
			String topicInfo = "[Spread=" + this.distributionMap.getSpread(i) + ", Focus=" + this.distributionMap.getFocus(i) + "]";
			
			this.labelRectangles.add(new DistributionRectangle(labelX, labelY, classSize, classSize, topicInfo, i, this.semanticTopics[i]));
			labelY += classSize + classSpace;
		}
		
		this.yBound = labelY;
	}
	
	private void buildDistributionMap(List<String> classes, int packageX, int packageY) throws UnsufficientNumberOfColorsException {
		
		int classIndex = 0;
		int classX = packageX + packageStroke + classSpace;
		int classY = packageY + packageStroke + classSpace;
		
		for (String className : classes) {
			
			int classWidth = 2*classStroke + classSize;
			int classHeight = classWidth;
			
			int clusterIndex = this.distributionMap.getCluster(className);
			String[] topics = (clusterIndex != -1) ? this.semanticTopics[clusterIndex] : new String[]{};
			this.classRectangles.add(new DistributionRectangle(classX, classY, classWidth, classHeight, className, clusterIndex, topics));
			
			if (clusterIndex != -1)
				this.classLabelRectangles.add(new DistributionRectangle(classX+5, classY+15, classSize, classSize, String.valueOf(clusterIndex)));
			
			classX += classWidth + classSpace;
			
			classIndex++;
			if (classIndex == maxClasses) {
				classX = packageX + packageStroke + classSpace;
				classY += classHeight + classSpace;
				classIndex = 0;
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D graphics = (Graphics2D) g;
		
		// paint border
		graphics.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graphics.setColor(new Color(255, 255, 255));
		graphics.fill(this.externalView);
		
		// paint packages
		graphics.setColor(new Color(0, 0, 0));
		graphics.setStroke(new BasicStroke(packageStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (DistributionRectangle pckg : this.packageRectangles)
			graphics.draw(pckg);
		
		for (DistributionRectangle pckgLabel : this.pckgLabelRectangles) {
			graphics.setFont(graphics.getFont().deriveFont(10f));
			graphics.drawString(pckgLabel.getEntityName(), (int) pckgLabel.getX() /*+ classSize + packageSpace*/, (int) pckgLabel.getY() /*+ packageSpace + 2*classSpace*/);
		}
		
		// paint classes
		for (DistributionRectangle clazz : this.classRectangles) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setStroke(new BasicStroke(classStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			graphics.setColor(clazz.getColor());
			graphics.fill(clazz);
			
			if (clazz.hasBorder()) {
				graphics.setColor(new Color(0, 0, 0));
				graphics.setStroke(new BasicStroke(classStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				graphics.draw(clazz);
			}
		}
		
		graphics.setColor(new Color(0, 0, 0));
		for (DistributionRectangle classLabel : this.classLabelRectangles) {
			graphics.setFont(graphics.getFont().deriveFont(10f));
			graphics.drawString(classLabel.getEntityName(), (int) classLabel.getX(), (int) classLabel.getY());
		}
		
		// paint labels
		for (DistributionRectangle label : this.labelRectangles) {
			graphics.setColor(new Color(0, 0, 0));
			graphics.setStroke(new BasicStroke(classStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			graphics.setColor(label.getColor());
			graphics.fill(label);
			
			graphics.setColor(new Color(0, 0, 0));
			graphics.setFont(graphics.getFont().deriveFont(12f));
			graphics.drawString(label.getClusterTopics(), (int) label.getX() + classSize + packageSpace, (int) label.getY() + packageSpace + 2*classSpace);
		}
	}
	
	public void saveImage(File file) throws IOException {
		BufferedImage image = new BufferedImage(this.xBound, this.yBound, BufferedImage.TYPE_INT_RGB);
		this.paintComponent(image.getGraphics());

	    ImageIO.write(image, "PNG", new File(file.getAbsolutePath() + ".png"));
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
	
	private MouseMotionListener getMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				for (DistributionRectangle clazz : classRectangles)
					if (clazz.contains(e.getPoint())) {
						setToolTipText(clazz.toString());
						ToolTipManager.sharedInstance().mouseMoved(e);
						return;
					}
				
				for (DistributionRectangle pckg : packageRectangles)
					if (pckg.contains(e.getPoint())) {
						setToolTipText(pckg.toString());
						ToolTipManager.sharedInstance().mouseMoved(e);
						return;
					}
				
				for (DistributionRectangle lbl : labelRectangles)
					if (lbl.contains(e.getPoint())) {
						setToolTipText(lbl.getEntityName());
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