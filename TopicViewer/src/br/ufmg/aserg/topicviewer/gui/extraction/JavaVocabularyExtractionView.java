package br.ufmg.aserg.topicviewer.gui.extraction;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import br.ufmg.aserg.topicviewer.control.extraction.JavaVocabularyExtractionController;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class JavaVocabularyExtractionView extends AbstractView {
	
	private static final long serialVersionUID = -6406361372828231928L;
	
    private javax.swing.JButton addStopwordButton;
    private javax.swing.JButton restoreDefaultValuesButton;
    private javax.swing.JButton selectProjectsButton;
    private javax.swing.JCheckBox allInformationCheckBox;
    private javax.swing.JCheckBox includeCommentInfoCheckBox;
    private javax.swing.JCheckBox includeJavaDocInfoCheckBox;
    private javax.swing.JCheckBox filterStopwordsCheckBox;
    private javax.swing.JCheckBox filterSmallWordsCheckBox;
    private javax.swing.JLabel informationTypeLabel;
    private javax.swing.JLabel minimalTermLengthLabel;
    private javax.swing.JLabel projectsLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField minimalTermLengthField;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList projectList;
    
    private File[] projectPaths;
    
    public JavaVocabularyExtractionView() {
    	super();
    	initComponents();
    	initListeners();
    	setDefaultValues();
    	this.pack();
    }
    
    @SuppressWarnings("rawtypes")
	private void initComponents() {
    	
    	informationTypeLabel = new javax.swing.JLabel("Information Type");
    	allInformationCheckBox = new javax.swing.JCheckBox("All Information");
    	includeCommentInfoCheckBox = new javax.swing.JCheckBox("Include Comments");
    	includeJavaDocInfoCheckBox = new javax.swing.JCheckBox("Include JavaDoc");
    	jSeparator1 = new javax.swing.JSeparator();
    	
    	filterStopwordsCheckBox = new javax.swing.JCheckBox("Filter Stopwords");
    	addStopwordButton = new javax.swing.JButton("Add Stopword");
    	jSeparator2 = new javax.swing.JSeparator();

    	filterSmallWordsCheckBox = new javax.swing.JCheckBox("Filter Small Words");
    	minimalTermLengthLabel = new javax.swing.JLabel("Minimal Term Length");
    	minimalTermLengthField = new javax.swing.JTextField();
    	minimalTermLengthField.setText("4");
    	restoreDefaultValuesButton = new javax.swing.JButton("Restore Default Values");
    	
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        
        projectsLabel = new javax.swing.JLabel("Projects");
        selectProjectsButton = new javax.swing.JButton("Select");
        projectList = new javax.swing.JList();
        
        setTitle("Java Vocabulary Extraction");
        setToolTipText("Extracting Vocabulary From Java Source Code");
        setName("vocabularyExtraction");
        
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(startExecutionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(minimalTermLengthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minimalTermLengthField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(restoreDefaultValuesButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(informationTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(allInformationCheckBox)
                                    .addComponent(includeCommentInfoCheckBox)
                                    .addComponent(includeJavaDocInfoCheckBox)
                                    .addComponent(filterStopwordsCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addStopwordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(115, 115, 115)
                                .addComponent(filterSmallWordsCheckBox)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(informationTypeLabel)
                    .addComponent(allInformationCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includeCommentInfoCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includeJavaDocInfoCheckBox)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterStopwordsCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addStopwordButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterSmallWordsCheckBox)
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minimalTermLengthLabel)
                    .addComponent(minimalTermLengthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(restoreDefaultValuesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
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
                        .addComponent(projectsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectProjectsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectProjectsButton)
                    .addComponent(projectsLabel))
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
    }
    
    private void initListeners() {
    	selectProjectsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectProjectsActionPerformed(evt);
            }
        });
    	
    	allInformationCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	boolean selected = allInformationCheckBox.isSelected();
				includeCommentInfoCheckBox.setSelected(selected);
				includeJavaDocInfoCheckBox.setSelected(selected);
            }
        });
    	
    	includeCommentInfoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
				allInformationCheckBox.setSelected(includeCommentInfoCheckBox.isSelected() && includeJavaDocInfoCheckBox.isSelected());
			}
		});
    	
    	includeJavaDocInfoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
				allInformationCheckBox.setSelected(includeJavaDocInfoCheckBox.isSelected() && includeCommentInfoCheckBox.isSelected());
			}
		});
    	
    	filterStopwordsCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				addStopwordButton.setEnabled(evt.getStateChange() == ItemEvent.SELECTED);
			}
		});
    	
    	filterSmallWordsCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				minimalTermLengthField.setEnabled(evt.getStateChange() == ItemEvent.SELECTED);
			}
		});
    	
    	addStopwordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStopwordActionPerformed(evt);
            }
        });
    	
    	restoreDefaultValuesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setDefaultValues();
            }
        });
    	
    	startExecutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startExtractionActionPerformed(evt);
            }
        });
    }
    
    private void setDefaultValues() {
    	allInformationCheckBox.setSelected(true);
    	includeCommentInfoCheckBox.setSelected(true);
    	includeJavaDocInfoCheckBox.setSelected(true);
    	
    	filterStopwordsCheckBox.setSelected(true);
//    	resetStopwords(); // TODO
    	
    	filterSmallWordsCheckBox.setSelected(true);
    	minimalTermLengthField.setText("4");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectProjectsActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.WORKSPACE)));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        if (chooser.showDialog(this, "Open") != JFileChooser.CANCEL_OPTION) {
            Vector vector = new Vector();
            for (File f : chooser.getSelectedFiles())
                vector.addElement(f);
            
            this.projectPaths = (File[]) vector.toArray(new File[0]);
            this.projectList.setListData(vector);
            this.projectList.setSelectionInterval(0, vector.size());
        }
    }
    
    private void addStopwordActionPerformed(java.awt.event.ActionEvent evt) { 
    	
    }
    
    @SuppressWarnings("deprecation")
	private void startExtractionActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (projectPaths != null) {
            if (!startExecutionButton.getText().trim().isEmpty()) {
            	boolean includeComment = includeCommentInfoCheckBox.isSelected();
            	boolean includeJavaDoc = includeJavaDocInfoCheckBox.isSelected();
            	boolean removeStopwords = filterStopwordsCheckBox.isSelected();
            	boolean removeSmallWords = filterSmallWordsCheckBox.isSelected();
            	int minimalTermLength = 0;
            	
            	try {
					minimalTermLength = Integer.parseInt(minimalTermLengthField.getText());
					if (minimalTermLength < 0) throw new NumberFormatException("");
				} catch (NumberFormatException e) {
					minimalTermLength = 4;
					minimalTermLengthField.setText("4");
					JOptionPane.showMessageDialog(this, "Invalid Number. Using default value");
				}
            	
                this.controller = new JavaVocabularyExtractionController(projectPaths, includeComment, includeJavaDoc, removeStopwords, removeSmallWords, minimalTermLength);
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
    
    @Override
    @SuppressWarnings("unchecked")
    public void refresh() {
    	this.projectPaths = null;
    	this.projectList.setListData(new Vector<File>());
    	this.progressBar.setValue(0);
    	this.repaint();
    }
}