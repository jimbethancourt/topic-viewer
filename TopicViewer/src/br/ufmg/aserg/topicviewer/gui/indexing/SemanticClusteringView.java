package br.ufmg.aserg.topicviewer.gui.indexing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import br.ufmg.aserg.topicviewer.control.indexing.SemanticClusteringController;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class SemanticClusteringView extends AbstractView {
	
	private static final long serialVersionUID = -4088186556157508864L;
	
	private javax.swing.JButton restoreDefaultValuesButton;
    private javax.swing.JButton selectProjectsButton;
    private javax.swing.JCheckBox chooseBestThresholdCheckBox;
    private javax.swing.JLabel lowRankValueLabel;
    private javax.swing.JLabel hierarchicalClusteringLabel;
    private javax.swing.JLabel projectLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton numClustersButton;
    private javax.swing.JRadioButton similarityThresholdButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSlider numClustersSlider;
    private javax.swing.JSlider similarityThresholdSlider;
    private javax.swing.JTextField lowRankValueField;
    @SuppressWarnings("rawtypes")
	private javax.swing.JList projectList;
    
    private File[] termDocumentMatrixFiles;
	
    public SemanticClusteringView() {
    	super();
    	initComponents();
    	initListeners();
    	setDefaultValues();
    	this.pack();
    }
	
    @SuppressWarnings("rawtypes")
	private void initComponents() {

    	lowRankValueLabel = new javax.swing.JLabel("Low-Rank Value");
    	lowRankValueField = new javax.swing.JTextField("(t*d)^0.2");
    	jSeparator1 = new javax.swing.JSeparator();
    	
    	hierarchicalClusteringLabel = new javax.swing.JLabel("Hierarchical Clustering");
    	numClustersButton = new javax.swing.JRadioButton("Number of Clusters");
    	numClustersSlider = new javax.swing.JSlider(JSlider.HORIZONTAL, 5, 50, 9);
    	numClustersSlider.setMajorTickSpacing(5);
    	numClustersSlider.setPaintLabels(true);
    	numClustersSlider.setPaintTicks(true);
    	similarityThresholdButton = new javax.swing.JRadioButton("Similarity Threshold");
    	similarityThresholdSlider = new javax.swing.JSlider(JSlider.HORIZONTAL, 55, 75, 65);
    	similarityThresholdSlider.setMajorTickSpacing(5);
    	similarityThresholdSlider.setPaintLabels(true);
    	similarityThresholdSlider.setPaintTicks(true);
    	chooseBestThresholdCheckBox = new javax.swing.JCheckBox("Choose Best Threshold");
    	restoreDefaultValuesButton = new javax.swing.JButton("Restore Default Values");
    	
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        
        projectList = new javax.swing.JList();
        selectProjectsButton = new javax.swing.JButton("Select");
        projectLabel = new javax.swing.JLabel("Term-Document Matrices (.matrix)");

        setTitle("Semantic Clustering");
        setName("semanticClustering");
        setMaximizable(true);
        
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Parameter Setting ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(startExecutionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1)))
                .addGap(10, 10, 10))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numClustersSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(restoreDefaultValuesButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lowRankValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lowRankValueField))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(similarityThresholdButton)
                                    .addComponent(numClustersButton)
                                    .addComponent(hierarchicalClusteringLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(similarityThresholdSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(chooseBestThresholdCheckBox)
                        .addGap(18, 18, 18))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lowRankValueLabel)
                    .addComponent(lowRankValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hierarchicalClusteringLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(numClustersButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numClustersSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(similarityThresholdButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(similarityThresholdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chooseBestThresholdCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(restoreDefaultValuesButton)
                .addGap(42, 42, 42)
                .addComponent(startExecutionButton)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Project Selection ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12))); // NOI18N
        jScrollPane1.setViewportView(projectList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(projectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectProjectsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectProjectsButton)
                    .addComponent(projectLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }
    
    private void initListeners() {
    	restoreDefaultValuesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultValues();
            }
        });
    	
    	numClustersButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
				similarityThresholdButton.setSelected(!selected);
				similarityThresholdSlider.setEnabled(!selected);
			}
		});
    	
    	similarityThresholdButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
				numClustersButton.setSelected(!selected);
				numClustersSlider.setEnabled(!selected);
				chooseBestThresholdCheckBox.setEnabled(selected);
			}
		});
    	
    	selectProjectsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectProjectsActionPerformed(evt);
            }
        });
    	
    	startExecutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startClusteringActionPerformed(evt);
            }
        });
    }
    
    private void setDefaultValues() {
    	lowRankValueField.setText("(t*d)^0.2");
    	
    	similarityThresholdButton.setSelected(true);
    	similarityThresholdSlider.setValue(65);
    	
    	chooseBestThresholdCheckBox.setSelected(false);
    }

    private Integer getLowRankValue() {
    	try {
			return Integer.parseInt(lowRankValueField.getText());
		} catch (NumberFormatException e) {
			if (!lowRankValueField.getText().equals("(t*d)^0.2")) {
				lowRankValueField.setText("(t*d)^0.2");
				JOptionPane.showMessageDialog(this, "Invalid Number. Using default value");
			}
			return 0;
		}
    }

    @SuppressWarnings("deprecation")
	private void startClusteringActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (termDocumentMatrixFiles != null) {
            if (!startExecutionButton.getText().trim().isEmpty()) {
            	int lowRankValue = getLowRankValue();
            	boolean useThreshold = similarityThresholdButton.isSelected();
            	boolean chooseBestThreshold = chooseBestThresholdCheckBox.isSelected();
            	int numClusters = numClustersSlider.getValue();
            	double threshold = (double) similarityThresholdSlider.getValue() * 0.01;
            	
                this.controller = new SemanticClusteringController(termDocumentMatrixFiles, lowRankValue, useThreshold, chooseBestThreshold, numClusters, threshold);
                this.progressMonitorThread = new Thread(this.controller);
                startExecutionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../../img/wait.gif")));
                startExecutionButton.setText("");
                this.repaint();
                this.pack();
                this.progressMonitorThread.start();
                new Thread(new PanelUpdater()).start();
            } else {
                startExecutionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../../img/start.png")));
                startExecutionButton.setText("Start");
                startExecutionButton.repaint();
                progressMonitorThread.stop();
            }
        }
    }                                       

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectProjectsActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.WORKSPACE)));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return (f.getName().endsWith(".matrix") && !f.getName().toLowerCase().endsWith("-lsi.matrix")) || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Term-Document Matrix Files (.matrix)";
            }
        });
        
        if (chooser.showDialog(this, "Open") != JFileChooser.CANCEL_OPTION) {
            Vector vector = new Vector();
            for (File f : chooser.getSelectedFiles())
                vector.addElement(f);
            
            this.termDocumentMatrixFiles = (File[]) vector.toArray(new File[0]);
            this.projectList.setListData(vector);
            this.projectList.setSelectionInterval(0, vector.size());
        }
    }  

    @Override
    @SuppressWarnings("unchecked")
    public void refresh() {
    	this.setDefaultValues();
    	this.termDocumentMatrixFiles = null;
    	this.projectList.setListData(new Vector<File>());
    	this.progressBar.setValue(0);
    	this.repaint();
    }
}