package br.ufmg.aserg.topicviewer.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.DustSkin;

import br.ufmg.aserg.topicviewer.gui.correlation.CorrelationMatrixViewer;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapViewer;
import br.ufmg.aserg.topicviewer.gui.extraction.JavaVocabularyExtractionView;
import br.ufmg.aserg.topicviewer.gui.indexing.SemanticClusteringView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class TopicViewer extends javax.swing.JFrame implements PanelUpdateListener {

	private static final long serialVersionUID = -7144305228539220119L;
	
    private static final String EXTRACT_VOCABULARY_PANEL = "vocabularyExtraction";
    private static final String SEMANTIC_CLUSTERING_PANEL = "semanticClustering";
    private static final String CORRELATION_MATRIX_VIEWER_PANEL = "correlationMatrixViewer";
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
    private JMenuItem correlationMatrixLoadItem;
    private JMenuItem distributionMapLoadItem;
    private JMenuItem exitItem;
    private JMenu aboutMenu;
    private JMenu helpMenu;
    private JMenuItem generalHelpItem;
    private JMenuItem paramenterHelpMenuItem;
    
    private Map<String, AbstractView> internalFrames;
	
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
        correlationMatrixLoadItem = new JMenuItem("Correlation Matrix");
        distributionMapLoadItem = new JMenuItem("Distribution Map");
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        
        aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('b');
        
        helpMenu = new JMenu("Help");
        generalHelpItem = new JMenuItem("General Information");
        paramenterHelpMenuItem = new JMenuItem("About the Parameters");
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
        viewResultsMenu.add(correlationMatrixLoadItem);
        viewResultsMenu.add(distributionMapLoadItem);
        activitiesMenu.add(itemSeparator);
        activitiesMenu.add(exitItem);
        
        helpMenu.add(generalHelpItem);
        helpMenu.add(paramenterHelpMenuItem);

        mainMenuBar.add(activitiesMenu);
        mainMenuBar.add(aboutMenu);
        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);
        
        setSize(800, 600);
		setPreferredSize(new Dimension(800, 600));
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
    	
    	correlationMatrixLoadItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCorrelationMatrixViewerActionPerformed(evt);
            }
        });
    	
    	distributionMapLoadItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDistributionMapViewerActionPerformed(evt);
            }
        });
    	
    	aboutMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuAboutActionPerformed(evt);
            }
        });
    	
    	generalHelpItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGeneralHelpActionPerformed(evt);
            }
        });
    	
    	paramenterHelpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuParameterHelpActionPerformed(evt);
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
    		extractVocabulary.addListener(this);
    		this.internalFrames.put(EXTRACT_VOCABULARY_PANEL, extractVocabulary);
    		this.desktop.add(extractVocabulary);
    	}
    }
    
    private void menuDocumentIndexingActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(SEMANTIC_CLUSTERING_PANEL)) {
    		SemanticClusteringView semanticClustering = new SemanticClusteringView();
    		this.internalFrames.put(SEMANTIC_CLUSTERING_PANEL, semanticClustering);
    		this.desktop.add(semanticClustering);
    	}
    }
    
    private void menuCorrelationMatrixViewerActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(CORRELATION_MATRIX_VIEWER_PANEL)) {
    		CorrelationMatrixViewer correlationMatrixViewer = new CorrelationMatrixViewer();
    		this.internalFrames.put(CORRELATION_MATRIX_VIEWER_PANEL, correlationMatrixViewer);
    		this.desktop.add(correlationMatrixViewer);
    	}
    }
    
    private void menuDistributionMapViewerActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(DISTRIBUTION_MAP_VIEWER_PANEL)) {
    		DistributionMapViewer distributionMapViewer = new DistributionMapViewer();
    		this.internalFrames.put(DISTRIBUTION_MAP_VIEWER_PANEL, distributionMapViewer);
    		this.desktop.add(distributionMapViewer);
    	}
    }
    
    private void menuAboutActionPerformed(MouseEvent evt) {
		JOptionPane.showMessageDialog(this, 
				"Welcome to TopicViewer. This open-source tool allows\n" +
				"text extraction from Java source code, and displays do-\n" +
				"main concepts across a system's package structure. All\n" +
				"generated files are stored in a folder given by the user\n" +
				"and are available for consulting. Our code is available \n" +
				"as a GoogleCode project at:\n"+
				">> http://code.google.com/p/topic-viewer/\n\n" +
				"This tool was developed by Gustavo Jansen de S. Santos \n" +
				"from Federal University of Minas Gerais, along with the\n" +
				"Software Practices Lab of Federal University of Campina\n" +
				"Grande. All information about our research can be ob- \n" +
				"tained on LINK.\n\n" +
				"Our research's been supported by CAPES, FAPEMIG and \n" +
				"CNPq.", "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void menuGeneralHelpActionPerformed(java.awt.event.ActionEvent evt) {
    	JOptionPane.showMessageDialog(this, 
				"The first activity available for a user, as he starts this tool, is configuring\n" +
				"a workspace, i.e. a folder which any output data will be stored.\n\n" +
				"All remaining available tasks in TopicViewer are divided as Extraction and\n" +
				"Displaying. The Extraction tasks include text extraction from Java source \n" +
				"code, resulting in a term-document matrix (two files with extensions .ma-\n" +
				"trix and .ids), along with a .vocabulary file containing information about\n" +
				"text and structure of one system.\n\n" +
				"The second Extraction process is Semantic Clustering, which groups si-\n" +
				"milar classes and extracts a set of frequent terms from these groups. This\n" +
				"process will generate four files: a correlation matrix (.cmatrix) with simi-\n" +
				"larities between all classes, pairwise; an .ids file with all class names; a\n" +
				".clusters file with the clustering output (all groups of classes); and final-\n" +
				"ly a text file displaying the frequent words -or semantic topics- from all\n" +
				"generated groups, in a .topics file. For visualization purposes, no file can\n" +
				"be deleted, as every information saved in files are important for analysis\n\n" +
				"Finally, the Displaying tasks include two kinds of visualization: the Cor-\n" +
				"relation Matrix View allows the user to view how the classes of a system \n" +
				"are similar to each other. The visualization is a simple matrix in which \n" +
				"darker colors represent higher cosine similarities between two class vo-\n" +
				"cabularies. On the other hand, the Distribution Map View shows the sys-\n" +
				"tem's package organization in boxes, and each class is represented in a\n" +
				"color referencing the group to which it was assigned. In this visualization,\n" +
				"quality metric values can be viewed as well, in a tooltip soon as a entity\n" +
				"is selected. Both visualization can be saved in a PNG file with the right\n" +
				"click of the mouse.", "General Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void menuParameterHelpActionPerformed(java.awt.event.ActionEvent evt) {
    	JOptionPane.showMessageDialog(this, 
				"Java Vocabulary Extraction:\n" +
				"- Information Type: Selects if comments and documentation in JavaDoc\n" +
				"will be included in the class vocabulary\n" +
				"- Filter Stopwords: A previously generated list of words too common in\n" +
				"English language will be dismissed in the extraction\n" +
				"- Filter Small Words: Any word with size smaller than the given value\n" + 
				"will be dismissed in the extraction\n\n" +
				"Semantic Clustering:\n" +
				"- Low-Rank Value: Number of lines of the reduced term-document\n" +
				"matrix. This value can be replaced by any positive number\n" +
				"- Similarity Threshold: As an agglomerative clustering, if no cluster \n" +
				"has similarity greater than the threshold with another cluster, the al-\n" +
				"gorithm will stop\n" +
				"- Choose Best Threshold: Chooses the best value among the given\n" +
				"values, according to internal vocabulary cohesion of the resulting\n" +
				"clusters. This process will consume a considering amount of time\n" +
				"Distribution Map View:\n" +
				"- Class Ordering: orders the classes alphabetically or groups classes\n" +
				"with groups -or colors- in common", "Parameter Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.gc();
        System.exit(0);
        
        // TODO
    }
    
    private void verifyProperties() {
    	Properties.load();
    	
    	String workspaceFolderName = Properties.getProperty(Properties.WORKSPACE);
    	if (workspaceFolderName == null || !new File(workspaceFolderName).exists())
    		enableButtons(false);
    }
    
    private void enableButtons(boolean enable) {
    	vocabularyExtractionMenu.setEnabled(enable);
    	javaVocabularyExtractionItem.setEnabled(enable);
    	semanticClusteringItem.setEnabled(enable);
    	viewResultsMenu.setEnabled(enable);
    	correlationMatrixLoadItem.setEnabled(enable);
    	distributionMapLoadItem.setEnabled(enable);
    }
    
    private boolean invokeView(String viewId) {
    	if (this.internalFrames.containsKey(viewId)) {
    		this.internalFrames.get(viewId).refresh();
    		this.internalFrames.get(viewId).show();
    		return true;
    	}
    	else return false;
    }
    
    public void notifyNewWindow(JInternalFrame frame) {
    	this.desktop.add(frame);
    }

    public static void main(String args[]) {
    	
        try {
        	JFrame.setDefaultLookAndFeelDecorated(true);
        	SwingUtilities.invokeLater(new Runnable() {

    			@Override
    			public void run() {
    				SubstanceLookAndFeel.setSkin(new DustSkin());
    				TopicViewer frame = new TopicViewer();
                	
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