package br.ufmg.aserg.topicviewer.gui.distribution;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import br.ufmg.aserg.topicviewer.control.distribution.DistributionMap;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import br.ufmg.aserg.topicviewer.control.distribution.ComparativeDistributionMapController;
import br.ufmg.aserg.topicviewer.control.distribution.DistributionMapCalculator;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class DistributionMapViewer extends AbstractView {
	
	private static final long serialVersionUID = -5260758202187415775L;
	
    private javax.swing.JTextField firstFileField;
    private javax.swing.JTextField secondFileField;
    private javax.swing.JButton firstFileSelectButton;
    private javax.swing.JButton secondFileSelectButton;
    private javax.swing.JPanel fileSelectionPanel;
    private javax.swing.JPanel classOrderingPanel;
    private javax.swing.JRadioButton comparativeButton;
    private javax.swing.JRadioButton nameOrderingButton;
    private javax.swing.JRadioButton topicOrderingButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    
    public DistributionMapViewer() {
    	super();
        initComponents();
        initListeners();
        this.pack();
    }

    private void initComponents() {

        fileSelectionPanel = new javax.swing.JPanel();
        firstFileField = new javax.swing.JTextField();
        firstFileSelectButton = new javax.swing.JButton("Select");
        jSeparator1 = new javax.swing.JSeparator();
        comparativeButton = new javax.swing.JRadioButton("Compare To");
        secondFileField = new javax.swing.JTextField();
        secondFileSelectButton = new javax.swing.JButton("Select");
        
        classOrderingPanel = new javax.swing.JPanel();
        nameOrderingButton = new javax.swing.JRadioButton("By Name");
        topicOrderingButton = new javax.swing.JRadioButton("By Semantic Topic");
        
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        
        setResizable(true);
        setMaximizable(true);
        setTitle("Distribution Map View");
        setToolTipText("Extracting Vocabulary From Java Source Code");
        setName("distributionMapView");

        fileSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Project File Selection (.matrix) ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 12))); // NOI18N

        firstFileField.setEditable(false);
        comparativeButton.setText("Compare To");
        secondFileField.setEditable(false);
        secondFileSelectButton.setEnabled(false);
        nameOrderingButton.setSelected(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(fileSelectionPanel);
        fileSelectionPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(comparativeButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(firstFileSelectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(firstFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(secondFileSelectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(secondFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(firstFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(firstFileSelectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comparativeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secondFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secondFileSelectButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), " Distribution Maps "));
        jScrollPane1.setViewportView(jTabbedPane1);

        classOrderingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Class Ordering "));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(classOrderingPanel);
        classOrderingPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameOrderingButton)
                    .addComponent(topicOrderingButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameOrderingButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(topicOrderingButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fileSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(classOrderingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fileSelectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(classOrderingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 136, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }

    private void initListeners() {
    	firstFileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDistributionMapActionPerformed(evt);
            }
        });
    	
    	comparativeButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
				secondFileSelectButton.setEnabled(selected);
				nameOrderingButton.setSelected(selected);
				checkFirstFileSelected();
			}
		});
    	
    	secondFileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectComparativeDistributionMapActionPerformed(evt);
            }
        });
    	
    	nameOrderingButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
				topicOrderingButton.setSelected(!selected);
			}
		});
    	
    	topicOrderingButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
				nameOrderingButton.setSelected(!selected);
			}
		});
    }
    
    private void checkFirstFileSelected() {
    	if (firstFileField.getText() == null || firstFileField.getText().isEmpty())
    		JOptionPane.showMessageDialog(null, "Please select the first project.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private JFileChooser getFileChooser() {
    	JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.WORKSPACE)));
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".cmatrix") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Correlation Matrix Files (.cmatrix)";
            }
        });
        
        return chooser;
    }

	private void selectDistributionMapActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = getFileChooser();
        
        if (chooser.showDialog(this, "Open") != JFileChooser.CANCEL_OPTION) {
        	File selectedFile = chooser.getSelectedFile();
            this.firstFileField.setText(selectedFile.getAbsolutePath());
            
            try {
            	this.jTabbedPane1.setVisible(true);
            	
            	String projectName = selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('.'));
            	String projectSimpleName = projectName.substring(projectName.lastIndexOf(File.separatorChar)+1);
            	
            	String idsFileName = projectName + ".ids";
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	
            	String termDocFileName = projectName.replace(Properties.CORRELATION_MATRIX_OUTPUT, Properties.TERM_DOC_MATRIX_OUTPUT) + "-lsi.matrix";
            	DoubleMatrix2D termDocumentMatrix = new DoubleMatrix2D(termDocFileName);
            	
            	String topicsFileName = projectName + ".topics";
            	String[][] semanticTopics = FileUtilities.readSemanticTopics(topicsFileName);
            	
            	String clustersFileName = projectName + ".clusters";
        		int[][] clusters = FileUtilities.readClustering(clustersFileName);
            	
            	DistributionMap distributionMap = DistributionMapCalculator.generateDistributionMap(projectSimpleName, termDocumentMatrix, documentIds, clusters, nameOrderingButton.isSelected());
            	DistributionMapPanel graphicPanel = new DistributionMapPanel(distributionMap, semanticTopics);
            	
            	graphicPanel.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
            	this.jTabbedPane1.addTab(projectSimpleName, graphicPanel);
            	this.jTabbedPane1.setSelectedComponent(graphicPanel);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Distribution Map Displaying Failed:\nCause:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
        }
    }
	
	private void selectComparativeDistributionMapActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = getFileChooser();
        
        if (chooser.showDialog(this, "Open") != JFileChooser.CANCEL_OPTION) {
        	File selectedFile = chooser.getSelectedFile();
            this.secondFileField.setText(selectedFile.getAbsolutePath());
            
            try {
            	this.jTabbedPane1.setVisible(true);
            	
            	String firstFile = firstFileField.getText();
            	firstFile = firstFile.substring(0, firstFile.lastIndexOf('.'));
            	String firstFileSimple = firstFile.substring(firstFile.lastIndexOf(File.separatorChar)+1);
            	String[][] firstSemanticTopics = FileUtilities.readSemanticTopics(firstFile + ".topics");
            	
            	String secondFile = secondFileField.getText();
            	secondFile = secondFile.substring(0, secondFile.lastIndexOf('.'));
            	String secondFileSimple = secondFile.substring(secondFile.lastIndexOf(File.separatorChar)+1);
            	String[][] secondSemanticTopics = FileUtilities.readSemanticTopics(secondFile + ".topics");
            	
            	ComparativeDistributionMapController controller = new ComparativeDistributionMapController(new String[] {firstFile, secondFile});
            	DistributionMap[] maps = controller.getDistributionMaps();
            	
            	JSplitPane jSplitPane1 = new javax.swing.JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            			new DistributionMapPanel(maps[0], firstSemanticTopics),
            			new DistributionMapPanel(maps[1], secondSemanticTopics));
            	
            	jSplitPane1.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, Boolean.TRUE);
            	this.jTabbedPane1.addTab(firstFileSimple + " - " + secondFileSimple, jSplitPane1);
            	this.jTabbedPane1.setSelectedComponent(jSplitPane1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Distribution Map Displaying Failed:\nCause:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
        }
    }

	@Override
	public void refresh() {
		this.jTabbedPane1.setVisible(false);
		this.firstFileField.setText("");
		this.secondFileField.setText("");
		this.comparativeButton.setSelected(false);
		this.secondFileSelectButton.setEnabled(false);
		this.nameOrderingButton.setSelected(true);
	}
}