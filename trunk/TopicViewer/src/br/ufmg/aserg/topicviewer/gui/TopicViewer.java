package br.ufmg.aserg.topicviewer.gui;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.DustSkin;

import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapViewer;
import br.ufmg.aserg.topicviewer.gui.extraction.JavaVocabularyExtractionView;
import br.ufmg.aserg.topicviewer.gui.indexing.SemanticClusteringView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class TopicViewer extends javax.swing.JFrame {

	private static final long serialVersionUID = -7144305228539220119L;
	
    private static final String EXTRACT_VOCABULARY_PANEL = "vocabularyExtraction";
    private static final String SEMANTIC_CLUSTERING_PANEL = "semanticClustering";
//    private static final String CORRELATION_MATRIX_VIEWER_PANEL = "correlationMatrixViewer";
    private static final String DISTRIBUTION_MAP_VIEWER_PANEL = "distributionMapViewer";
	
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JPopupMenu.Separator itemSeparator;
    
    private JMenu activitiesMenu;
    private JMenuItem configureWorkspaceItem;
    private JMenu vocabularyExtractionMenu;
    private JMenuItem javaVocabularyExtractionItem;
    private JMenuItem semanticClusteringItem;
    private JMenu viewResultsMenu;
    private JMenuItem distributionMapLoadItem;
    private JMenuItem exitItem;
    private JMenu aboutMenu;
    private JMenu helpMenu;
    
    private Map<String, AbstractView> internalFrames;
	
    // TODO implementar logging, tratamento de erros e mensagens de tooltip
    
	public TopicViewer() {
        initComponents();
        initListeners();	
        verifyProperties();
        
        this.internalFrames = new HashMap<String, AbstractView>();
    }

    private void initComponents() {
    	setTitle("TopicViewer");
        desktop = new javax.swing.JDesktopPane();
        
        mainMenuBar = new javax.swing.JMenuBar();
    	itemSeparator = new javax.swing.JPopupMenu.Separator();
        itemSeparator.setBackground(new java.awt.Color(0, 0, 0));
    	
    	activitiesMenu = new JMenu("Activities");
        activitiesMenu.setMnemonic('A');
        
        configureWorkspaceItem = new JMenuItem("Configure Workspace");
        vocabularyExtractionMenu = new JMenu("Vocabulary Extraction");
        javaVocabularyExtractionItem = new JMenuItem("Java Vocabulary Extraction");
        semanticClusteringItem = new JMenuItem("Semantic Clustering");
        viewResultsMenu = new JMenu("View Results");
        distributionMapLoadItem = new JMenuItem("Distribution Map");
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        
        aboutMenu = new javax.swing.JMenu("About");
        aboutMenu.setMnemonic('b');
        
        helpMenu = new javax.swing.JMenu("Help");
        helpMenu.setMnemonic('H');        
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setFont(new java.awt.Font("DejaVu Sans", 0, 12));
        setLocationByPlatform(true);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));
        getContentPane().add(desktop);

        activitiesMenu.add(configureWorkspaceItem);
        activitiesMenu.add(vocabularyExtractionMenu);
        vocabularyExtractionMenu.add(javaVocabularyExtractionItem);
        activitiesMenu.add(semanticClusteringItem);
        activitiesMenu.add(viewResultsMenu);
        viewResultsMenu.add(distributionMapLoadItem);
        activitiesMenu.add(itemSeparator);
        activitiesMenu.add(exitItem);

        mainMenuBar.add(activitiesMenu);
        mainMenuBar.add(aboutMenu);
        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);
    }
    
    private void initListeners() {
    	configureWorkspaceItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfigureWorkspaceActionPerformed(evt);
            }
        });
    	
    	javaVocabularyExtractionItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExtractVocabularyActionPerformed(evt);
            }
        });
    	
    	semanticClusteringItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDocumentIndexingActionPerformed(evt);
            }
        });
    	
//    	correlationViewLoadItem.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                menuCorrelationMatrixViewerActionPerformed(evt);
//            }
//        });
    	
    	distributionMapLoadItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDistributionMapViewerActionPerformed(evt);
            }
        });
    	
    	exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
    }

    private void menuConfigureWorkspaceActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        if (chooser.showDialog(this, "Open Workspace") != JFileChooser.CANCEL_OPTION) {
        	Map<String, String> properties = new HashMap<String, String>();
        	properties.put(Properties.WORKSPACE, chooser.getSelectedFile().getAbsolutePath());
        	Properties.setProperties(properties);
        	enableButtons(true);
        	JOptionPane.showMessageDialog(this, "Workspace has just been configured.", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void menuExtractVocabularyActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(EXTRACT_VOCABULARY_PANEL)) {
    		JavaVocabularyExtractionView extractVocabulary = new JavaVocabularyExtractionView();
    		this.internalFrames.put(EXTRACT_VOCABULARY_PANEL, extractVocabulary);
    		this.desktop.add(extractVocabulary, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
    private void menuDocumentIndexingActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(SEMANTIC_CLUSTERING_PANEL)) {
    		SemanticClusteringView semanticClustering = new SemanticClusteringView();
    		this.internalFrames.put(SEMANTIC_CLUSTERING_PANEL, semanticClustering);
    		this.desktop.add(semanticClustering, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
//    private void menuCorrelationMatrixViewerActionPerformed(java.awt.event.ActionEvent evt) {
//    	if (!invokeView(CORRELATION_MATRIX_VIEWER_PANEL)) {
//    		CorrelationMatrixViewer correlationMatrixViewer = new CorrelationMatrixViewer();
//    		this.internalFrames.put(CORRELATION_MATRIX_VIEWER_PANEL, correlationMatrixViewer);
//    		this.desktop.add(correlationMatrixViewer, javax.swing.JLayeredPane.DEFAULT_LAYER);
//    	}
//    }
    
    private void menuDistributionMapViewerActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(DISTRIBUTION_MAP_VIEWER_PANEL)) {
    		DistributionMapViewer distributionMapViewer = new DistributionMapViewer();
    		this.internalFrames.put(DISTRIBUTION_MAP_VIEWER_PANEL, distributionMapViewer);
    		this.desktop.add(distributionMapViewer, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.gc();
        System.exit(0);
    }
    
    private void verifyProperties() {
    	Properties.load();
    	
    	String workspaceFolderName = Properties.getProperty(Properties.WORKSPACE);
    	if (workspaceFolderName == null || !new File(workspaceFolderName).exists())
    		enableButtons(false);
    }
    
    private void enableButtons(boolean enable) {
//    	extractVocabularyMenuItem.setEnabled(enable);
//    	documentIndexingMenuItem.setEnabled(enable);
//    	correlationMatrixMenu.setEnabled(enable);
//    	correlationMatrixCalculatorMenuItem.setEnabled(enable);
//    	correlationMatrixClustererMenuItem.setEnabled(enable);
//    	correlationMatrixViewerMenuItem.setEnabled(enable);
//    	distributionMapViewerMenuItem.setEnabled(enable);
//    	conceptualMetricsCalculatorMenuItem.setEnabled(enable);
    }
    
    private boolean invokeView(String viewId) {
    	if (this.internalFrames.containsKey(viewId)) {
    		this.internalFrames.get(viewId).refresh();
    		this.internalFrames.get(viewId).show();
    		return true;
    	}
    	else return false;
    }

    public static void main(String args[]) {
    	
        try {
        	JFrame.setDefaultLookAndFeelDecorated(true);
        	SwingUtilities.invokeLater(new Runnable() {

    			@Override
    			public void run() {
    				SubstanceLookAndFeel.setSkin(new DustSkin());
    				TopicViewer frame = new TopicViewer();
    				frame.setSize(800, 600);
                	
        			BufferedImage image = null;
        			try {
        				image = ImageIO.read(frame.getClass().getResource("../img/icon.png"));
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			
        			frame.setIconImage(image);
                    frame.setVisible(true);
    			}
        		
        	});
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TopicViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }
}