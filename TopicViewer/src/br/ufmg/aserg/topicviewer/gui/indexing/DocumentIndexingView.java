package br.ufmg.aserg.topicviewer.gui.indexing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;

import br.ufmg.aserg.topicviewer.control.indexing.DocumentIndexingController;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class DocumentIndexingView extends AbstractView {

	private static final long serialVersionUID = 8181631458226590601L;
	
	private javax.swing.JPanel optionsPanel;
	private javax.swing.JLabel weightingLabel;
	private javax.swing.JRadioButton weightingTFRadioButton;
	private javax.swing.JRadioButton weightingIDFRadioButton;
	private javax.swing.JRadioButton weightingTFIDFRadioButton;
	private List<JRadioButton> weightingButtonList;
	private javax.swing.JSeparator panelSeparator1;
	private javax.swing.JLabel termFrequencyLabel;
	private javax.swing.JRadioButton termFrequencyAbsRadioButton;
	private javax.swing.JRadioButton termFrequencyRelRadioButton;
	private List<JRadioButton> termFrequencyButtonList;
	private javax.swing.JSeparator panelSeparator2;
	private javax.swing.JLabel lowRankLabel;
	private javax.swing.JTextField lowRankTextField;
	private javax.swing.JButton setDefaultValuesButton;
	
	@SuppressWarnings("rawtypes")
	private javax.swing.JList projectList;
    private javax.swing.JButton selectProjectsButton;
    private javax.swing.JLabel projectSelectionLabel;
    private javax.swing.JPanel projectSelectionPanel;
    private javax.swing.JScrollPane projectScrollPane;
	
    private File[] vocabularyFiles;

    public DocumentIndexingView() {
    	super();
        initComponents();
        initListeners();
        setDefaultValues();
        this.pack();
    }

    @SuppressWarnings("rawtypes")
    private void initComponents() {

        optionsPanel = new javax.swing.JPanel();
        setDefaultValuesButton = new javax.swing.JButton();
        weightingLabel = new javax.swing.JLabel();
        weightingTFRadioButton = new javax.swing.JRadioButton();
        weightingIDFRadioButton = new javax.swing.JRadioButton();
        weightingTFIDFRadioButton = new javax.swing.JRadioButton();
        
        termFrequencyLabel = new javax.swing.JLabel();
        termFrequencyAbsRadioButton = new javax.swing.JRadioButton();
        termFrequencyRelRadioButton = new javax.swing.JRadioButton();
        panelSeparator1 = new javax.swing.JSeparator();
        panelSeparator2 = new javax.swing.JSeparator();
        lowRankLabel = new javax.swing.JLabel();
        lowRankTextField = new javax.swing.JTextField();
        
        projectSelectionPanel = new javax.swing.JPanel();
        projectScrollPane = new javax.swing.JScrollPane();
        projectList = new javax.swing.JList();
        selectProjectsButton = new javax.swing.JButton();
        projectSelectionLabel = new javax.swing.JLabel();

        setTitle("Document Indexing with LSI");
        setName("documentIndexing");

        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Indexing Options ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12))); // NOI18N
        
        weightingLabel.setText("Term Weighting");
        weightingTFRadioButton.setText("TF");
        weightingTFRadioButton.setName("TF");
        weightingIDFRadioButton.setText("IDF");
        weightingIDFRadioButton.setName("IDF");
        weightingTFIDFRadioButton.setText("TF-IDF");
        weightingTFIDFRadioButton.setName("TFIDF");
        
        ButtonGroup weightingButtonGroup = new ButtonGroup();
        weightingButtonList = new LinkedList<JRadioButton>();
        weightingButtonGroup.add(weightingTFRadioButton);
        weightingButtonGroup.add(weightingIDFRadioButton);
        weightingButtonGroup.add(weightingTFIDFRadioButton);
        weightingButtonList.add(weightingTFRadioButton);
        weightingButtonList.add(weightingIDFRadioButton);
        weightingButtonList.add(weightingTFIDFRadioButton);
        
        termFrequencyLabel.setText("Term Frequency");
        termFrequencyAbsRadioButton.setText("Absolute");
        termFrequencyAbsRadioButton.setName("ABSOLUTE");
        termFrequencyRelRadioButton.setText("Relative");
        termFrequencyRelRadioButton.setName("RELATIVE");
        
        ButtonGroup termFrequencyButtonGroup = new ButtonGroup();
        termFrequencyButtonList = new LinkedList<JRadioButton>();
        termFrequencyButtonGroup.add(termFrequencyAbsRadioButton);
        termFrequencyButtonGroup.add(termFrequencyRelRadioButton);
        termFrequencyButtonList.add(termFrequencyAbsRadioButton);
        termFrequencyButtonList.add(termFrequencyRelRadioButton);
        
        lowRankLabel.setText("Low-Rank");

        setDefaultValuesButton.setText("Set Default");

        projectSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Project Selection ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12))); // NOI18N
        projectSelectionLabel.setText("Projects");
        selectProjectsButton.setText("Select");
        projectScrollPane.setViewportView(projectList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(setDefaultValuesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(startExecutionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(weightingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(weightingIDFRadioButton)
                                    .addComponent(weightingTFRadioButton)
                                    .addComponent(weightingTFIDFRadioButton)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(termFrequencyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(termFrequencyRelRadioButton)
                                    .addComponent(termFrequencyAbsRadioButton))))
                        .addGap(0, 34, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lowRankLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lowRankTextField)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weightingTFRadioButton)
                    .addComponent(weightingLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weightingIDFRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weightingTFIDFRadioButton)
                .addGap(12, 12, 12)
                .addComponent(panelSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(termFrequencyAbsRadioButton)
                    .addComponent(termFrequencyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(termFrequencyRelRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lowRankLabel)
                    .addComponent(lowRankTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(setDefaultValuesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addComponent(startExecutionButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(projectSelectionPanel);
        projectSelectionPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(projectScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(projectSelectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectProjectsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectProjectsButton)
                    .addComponent(projectSelectionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectScrollPane)
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
                .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(projectSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(projectSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }
    
    private void initListeners() {
        startExecutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startIndexingActionPerformed(evt);
            }
        });
        
        weightingIDFRadioButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
				termFrequencyAbsRadioButton.setEnabled(!selected);
				termFrequencyRelRadioButton.setEnabled(!selected);
			}
		});
    	
        setDefaultValuesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultValues();
            }
        });
        
        selectProjectsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectProjectsActionPerformed(evt);
            }
        });
    }
    
    private void setDefaultValues() {
    	weightingTFIDFRadioButton.setSelected(true);
    	termFrequencyRelRadioButton.setSelected(true);
    	lowRankTextField.setText("");
    }
    
    private String getWeightFunctionValue() {
    	for (JRadioButton button : weightingButtonList)
    		if (button.isSelected()) return button.getName();
    	return null;
    }
    
    private String getTFVariantValue() {
    	for (JRadioButton button : termFrequencyButtonList)
    		if (button.isSelected()) return button.getName();
    	return null;
    }
    
    private Integer getLowRankValue() {
    	try {
			return Integer.parseInt(lowRankTextField.getText());
		} catch (NumberFormatException e) {
			return 0; // TODO mensagem de erro
		}
    }

    @SuppressWarnings("deprecation")
	private void startIndexingActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (vocabularyFiles != null) {
            if (!startExecutionButton.getText().trim().isEmpty()) {
            	String weightFunction = getWeightFunctionValue();
            	String tfVariant = getTFVariantValue();
            	Integer lowRank = getLowRankValue();
            	
                this.controller = new DocumentIndexingController(vocabularyFiles, weightFunction, tfVariant, lowRank);
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
                return f.getName().toLowerCase().endsWith(".vxl") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Vocabulary Files (.vxl)";
            }
        });
        
        if (chooser.showDialog(this, "Open") != JFileChooser.CANCEL_OPTION) {
            Vector vector = new Vector();
            for (File f : chooser.getSelectedFiles())
                vector.addElement(f);
            
            this.vocabularyFiles = (File[]) vector.toArray(new File[0]);
            this.projectList.setListData(vector);
            this.projectList.setSelectionInterval(0, vector.size());
        }
    }  

    @Override
    @SuppressWarnings("unchecked")
    public void refresh() {
    	this.setDefaultValues();
    	this.vocabularyFiles = null;
    	this.projectList.setListData(new Vector<File>());
    	this.progressBar.setValue(0);
    	this.repaint();
    }
}