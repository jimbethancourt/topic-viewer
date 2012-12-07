package br.ufmg.aserg.topicviewer.gui.distribution;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.ufmg.aserg.topicviewer.control.distribution.DistributionMapCalculator;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class DistributionMapViewer extends AbstractView {
	
	private static final long serialVersionUID = -5260758202187415775L;
	
	private javax.swing.JTextField fileNameTextField;
    private javax.swing.JButton selectFileButton;
    private javax.swing.JTextPane detailsTextPane;
    private javax.swing.JPanel filePanel;
    private javax.swing.JPanel detailsPanel;
    private JScrollPane detailsScrollPane;
    private JScrollPane distributionMapScrollPane;
    
    public DistributionMapViewer() {
    	super();
        initComponents();
        initListeners();
        this.pack();
        this.detailsPanel.setVisible(false);
    }

    private void initComponents() {

        filePanel = new javax.swing.JPanel();
        fileNameTextField = new javax.swing.JTextField();
        detailsPanel = new javax.swing.JPanel();
        selectFileButton = new javax.swing.JButton();
        distributionMapScrollPane = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        detailsScrollPane = new javax.swing.JScrollPane();
        detailsTextPane = new javax.swing.JTextPane();

        setTitle("Distribution Map Viewer");
        setToolTipText("distributionView");
        setResizable(true);
        setMaximizable(true);
        
        filePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Matrix File Selection "));
        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Cluster Details "));
        distributionMapScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), " Distribution Map View "));

        detailsScrollPane.setViewportView(detailsTextPane);
        fileNameTextField.setEditable(false);
        selectFileButton.setText("Select");
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(filePanel);
        filePanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectFileButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(detailsScrollPane)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(detailsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(filePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(distributionMapScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(distributionMapScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }
    
    private void initListeners() {
    	selectFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectMatrixFileActionPerformed(evt);
            }
        });
    }

	private void selectMatrixFileActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.WORKSPACE)));
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".matrix") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Matrix Files (.matrix)";
            }
        });
        
        if (chooser.showDialog(this, "Open") != JFileChooser.CANCEL_OPTION) {
        	File selectedFile = chooser.getSelectedFile();
            this.fileNameTextField.setText(selectedFile.getAbsolutePath());
            
            try {
            	this.detailsPanel.setVisible(false);
            	
            	String projectName = selectedFile.getName().contains("clustered") ?
            			selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('-')) : 
            			selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('.'));
            	
            	String idsFileName = projectName + ".ids";
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	
            	String topicsFileName = projectName + ".topics";
            	String[][] semanticTopics = FileUtilities.readSemanticTopics(topicsFileName);
            	
            	String clustersFileName = projectName + ".clusters";
        		int[][] clusters = FileUtilities.readClustering(clustersFileName);
            	
            	DistributionMap distributionMap = DistributionMapCalculator.generateDistributionMap(documentIds, clusters);
            	DistributionMapGraphicPanel graphicPanel = new DistributionMapGraphicPanel(distributionMap, semanticTopics);
            	
                this.distributionMapScrollPane.setViewportView(graphicPanel);
                this.distributionMapScrollPane.revalidate();
                this.distributionMapScrollPane.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }                                        

	@Override
	public void refresh() {
		this.fileNameTextField.setText("");
		this.detailsPanel.setVisible(false);
		this.detailsTextPane.setText("");
		this.distributionMapScrollPane.setViewportView(new JPanel());
		this.distributionMapScrollPane.repaint();
	}
	
//	@Override
//	public void actionPerformed(Integer clusterIndex) {
//		String output = " === Cluster " + clusterIndex + " === " + SEPARATOR;
//		for (String topic : this.semanticTopics[clusterIndex])
//			output += topic + SEPARATOR;
//		this.detailsTextPane.setText(output);
//	}
}