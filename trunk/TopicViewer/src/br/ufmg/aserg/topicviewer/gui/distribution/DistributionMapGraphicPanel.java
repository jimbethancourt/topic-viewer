package br.ufmg.aserg.topicviewer.gui.distribution;

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

public class DistributionMapGraphicPanel extends JPanel {

	private static final long serialVersionUID = 6060506974958934930L;
	
	private static final Integer packageStroke = 5;
	private static final Integer packageSpace = 8;
	private static final Integer classStroke = 3;
	private static final Integer classSpace = 7;
	private static final Integer classSize = 20;
	
	private static final Integer maxPackages = 5;
	private static final Integer maxClasses = 5;
	
	private DistributionMap distributionMap;
	private String[][] semanticTopics;
	
	private Rectangle2D.Double externalView;
	private List<DistributionRectangle> packageRectangles;
	private List<DistributionRectangle> classRectangles;
	
	public DistributionMapGraphicPanel(DistributionMap distributionMap, String[][] semanticTopics) {
		super();
		
		this.distributionMap = distributionMap;
		this.semanticTopics = semanticTopics;
		
		this.packageRectangles = new LinkedList<DistributionRectangle>();
		this.classRectangles = new LinkedList<DistributionRectangle>();
		
		int bounds = (packageSpace*2) + (maxPackages-1)*packageSpace + 2*maxPackages*packageStroke 
				+ classSpace*2 + (maxClasses-1)*classSpace + 2*maxClasses*classStroke + maxClasses*classSize;
		this.externalView = new Rectangle2D.Double(5, 5, bounds, bounds);
		
		this.buildDistributionMap();
		this.addMouseMotionListener(getMouseMotionListener());
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(bounds, bounds));
		this.setPreferredSize(new Dimension(bounds, bounds));
	}
	
	private void buildDistributionMap() {
		
		int packageIndex = 0;
		int packageX = packageSpace;
		int packageY = packageSpace;
		int maxPackageHeight = 0;
		
		for (String packageName : this.distributionMap.getPackages()) {
			List<String> classes = this.distributionMap.getClasses(packageName);
			int numClasses = classes.size();
			
			int numColumns = Math.min(maxClasses, numClasses);
			int packageWidth = 2*packageStroke + 2*classSpace 
					+ numColumns*(2*classStroke + classSize) 
					+ (numColumns-1)*classSpace;
			int numLines = (int) Math.ceil(maxClasses / numClasses);
			int packageHeight = 2*packageStroke + 2*classSpace 
					+ numLines*(2*classStroke + classSize)
					+ (numLines-1)*classSpace;
			maxPackageHeight = Math.max(packageHeight, maxPackageHeight);
			
			this.packageRectangles.add(new DistributionRectangle(packageX, packageY, packageWidth, packageHeight, packageName));
			this.buildDistributionMap(classes, packageX, packageY);
			
			packageX += packageWidth + packageSpace;
			
			packageIndex++;
			if (packageIndex == maxPackages) {
				packageX = packageSpace;
				packageY = maxPackageHeight + packageSpace;
				maxPackageHeight = 0;
				packageIndex = 0;
			}
		}
	}
	
	private void buildDistributionMap(List<String> classes, int packageX, int packageY) {
		
		int classIndex = 0;
		int classX = packageX + classSpace;
		int classY = packageY + classSpace;
		
		for (String className : classes) {
			
			int classWidth = 2*classStroke + classSize;
			int classHeight = classWidth;
			
			int clusterIndex = this.distributionMap.getCluster(className);
			String[] topics = this.semanticTopics[clusterIndex];
			this.classRectangles.add(new DistributionRectangle(classX, classY, classWidth, classHeight, className, clusterIndex, topics));
			
			classX += classWidth + classSpace;
			
			classIndex++;
			if (classIndex == maxClasses) {
				classX = packageX;
				classY = classHeight + classSpace;
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
		graphics.fill(this.externalView);
		
		// paint packages
		graphics.setStroke(new BasicStroke(packageStroke));
		for (Rectangle2D.Double pckg : this.packageRectangles)
			graphics.draw(pckg);
		
		// paint classes
		graphics.setStroke(new BasicStroke(classStroke));
		for (DistributionRectangle clazz : this.classRectangles) {
			graphics.setColor(clazz.getColor());
			graphics.fill(clazz);
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
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		};
	}
}