package br.ufmg.aserg.topicviewer.gui;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import br.ufmg.aserg.topicviewer.gui.correlation.CorrelationMatrixCalculatorView;
import br.ufmg.aserg.topicviewer.gui.correlation.CorrelationMatrixClusteringView;
import br.ufmg.aserg.topicviewer.gui.correlation.CorrelationMatrixViewer;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapViewer;
import br.ufmg.aserg.topicviewer.gui.extraction.VocabularyExtractionView;
import br.ufmg.aserg.topicviewer.gui.indexing.DocumentIndexingView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class TopicViewer extends javax.swing.JFrame {

	private static final long serialVersionUID = -7144305228539220119L;
	
    private static final String EXTRACT_VOCABULARY_PANEL = "extract";
    private static final String DOCUMENT_INDEXING_PANEL = "indexing";
    private static final String CORRELATION_MATRIX_CALCULATOR_PANEL = "correlationCalculator";
    private static final String CORRELATION_MATRIX_CLUSTERER_PANEL = "correlationClusterer";
    private static final String CORRELATION_MATRIX_VIEWER_PANEL = "correlationViewer";
    private static final String DISTRIBUTION_MAP_VIEWER_PANEL = "distributionViewer";
	
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    
    private javax.swing.JMenu activitiesMenu;
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenu helpMenu;
    
    private javax.swing.JMenuItem configureWorkspaceMenuItem;
    private javax.swing.JMenuItem extractVocabularyMenuItem;
    private javax.swing.JMenuItem documentIndexingMenuItem;
    private javax.swing.JMenu correlationMatrixMenu;
    private javax.swing.JMenuItem correlationMatrixCalculatorMenuItem;
    private javax.swing.JMenuItem correlationMatrixClustererMenuItem;
    private javax.swing.JMenuItem correlationMatrixViewerMenuItem;
    private javax.swing.JMenuItem distributionMapViewerMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    
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
        activitiesMenu = new javax.swing.JMenu();
        activitiesMenu.setMnemonic('A');
        activitiesMenu.setText("Activities");
        
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        
        configureWorkspaceMenuItem = new javax.swing.JMenuItem();
        extractVocabularyMenuItem = new javax.swing.JMenuItem();
        documentIndexingMenuItem = new javax.swing.JMenuItem();
        correlationMatrixMenu = new javax.swing.JMenu();
        correlationMatrixCalculatorMenuItem = new javax.swing.JMenuItem();
        correlationMatrixClustererMenuItem = new javax.swing.JMenuItem();
        correlationMatrixViewerMenuItem = new javax.swing.JMenuItem();
        distributionMapViewerMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();

        configureWorkspaceMenuItem.setText("Configure Workspace");
        extractVocabularyMenuItem.setText("Extract Vocabulary");
        documentIndexingMenuItem.setText("Document Indexing with LSI");
        correlationMatrixMenu.setText("Correlation Matrix");
        correlationMatrixCalculatorMenuItem.setText("Correlation Matrix Calculator");
        correlationMatrixClustererMenuItem.setText("Correlation Matrix Clusterer");
        correlationMatrixViewerMenuItem.setText("Correlation Matrix Viewer");
        distributionMapViewerMenuItem.setText("Distribution Map Viewer");
        exitMenuItem.setText("Exit");
        
        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        
        aboutMenu = new javax.swing.JMenu();
        aboutMenu.setMnemonic('b');
        aboutMenu.setText("About");
        
        helpMenu = new javax.swing.JMenu();
        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setFont(new java.awt.Font("DejaVu Sans", 0, 12));
        setLocationByPlatform(true);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));
        getContentPane().add(desktop);

        activitiesMenu.add(configureWorkspaceMenuItem);
        activitiesMenu.add(extractVocabularyMenuItem);
        activitiesMenu.add(documentIndexingMenuItem);
        activitiesMenu.add(correlationMatrixMenu);
        correlationMatrixMenu.add(correlationMatrixCalculatorMenuItem);
        correlationMatrixMenu.add(correlationMatrixClustererMenuItem);
        correlationMatrixMenu.add(correlationMatrixViewerMenuItem);
        activitiesMenu.add(jSeparator1);
        activitiesMenu.add(distributionMapViewerMenuItem);
        activitiesMenu.add(jSeparator1);
        activitiesMenu.add(exitMenuItem);

        mainMenuBar.add(activitiesMenu);
        mainMenuBar.add(aboutMenu);
        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);
    }
    
    private void initListeners() {
    	configureWorkspaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfigureWorkspaceActionPerformed(evt);
            }
        });
    	
    	extractVocabularyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExtractVocabularyActionPerformed(evt);
            }
        });
    	
    	documentIndexingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDocumentIndexingActionPerformed(evt);
            }
        });
    	
    	correlationMatrixCalculatorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCorrelationMatrixCalculatorActionPerformed(evt);
            }
        });
    	
    	correlationMatrixClustererMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCorrelationMatrixClustererActionPerformed(evt);
            }
        });
    	
    	correlationMatrixViewerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCorrelationMatrixViewerActionPerformed(evt);
            }
        });
    	
    	distributionMapViewerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDistributionMapViewerActionPerformed(evt);
            }
        });
    	
    	exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
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
    		VocabularyExtractionView extractVocabulary = new VocabularyExtractionView();
    		this.internalFrames.put(EXTRACT_VOCABULARY_PANEL, extractVocabulary);
    		this.desktop.add(extractVocabulary, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
    private void menuDocumentIndexingActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(DOCUMENT_INDEXING_PANEL)) {
    		DocumentIndexingView documentIndexing = new DocumentIndexingView();
    		this.internalFrames.put(DOCUMENT_INDEXING_PANEL, documentIndexing);
    		this.desktop.add(documentIndexing, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
    private void menuCorrelationMatrixCalculatorActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(CORRELATION_MATRIX_CALCULATOR_PANEL)) {
    		CorrelationMatrixCalculatorView correlationMatrixCalculator = new CorrelationMatrixCalculatorView();
    		this.internalFrames.put(CORRELATION_MATRIX_CALCULATOR_PANEL, correlationMatrixCalculator);
    		this.desktop.add(correlationMatrixCalculator, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
    private void menuCorrelationMatrixClustererActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(CORRELATION_MATRIX_CLUSTERER_PANEL)) {
    		CorrelationMatrixClusteringView correlationMatrixClustering = new CorrelationMatrixClusteringView();
    		this.internalFrames.put(CORRELATION_MATRIX_CLUSTERER_PANEL, correlationMatrixClustering);
    		this.desktop.add(correlationMatrixClustering, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
    private void menuCorrelationMatrixViewerActionPerformed(java.awt.event.ActionEvent evt) {
    	if (!invokeView(CORRELATION_MATRIX_VIEWER_PANEL)) {
    		CorrelationMatrixViewer correlationMatrixViewer = new CorrelationMatrixViewer();
    		this.internalFrames.put(CORRELATION_MATRIX_VIEWER_PANEL, correlationMatrixViewer);
    		this.desktop.add(correlationMatrixViewer, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	}
    }
    
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
    	extractVocabularyMenuItem.setEnabled(enable);
    	documentIndexingMenuItem.setEnabled(enable);
    	correlationMatrixMenu.setEnabled(enable);
    	correlationMatrixCalculatorMenuItem.setEnabled(enable);
    	correlationMatrixClustererMenuItem.setEnabled(enable);
    	correlationMatrixViewerMenuItem.setEnabled(enable);
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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TopicViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TopicViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TopicViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TopicViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
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
    }
}