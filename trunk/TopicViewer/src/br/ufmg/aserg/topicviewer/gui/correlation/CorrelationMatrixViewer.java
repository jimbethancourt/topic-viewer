package br.ufmg.aserg.topicviewer.gui.correlation;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.gui.correlation.ClusteredCorrelationMatrixGraphicPanel.ClusteredMatrixListener;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class CorrelationMatrixViewer extends AbstractView implements ClusteredMatrixListener {
	
	private static final long serialVersionUID = -5260758202187415775L;
	
	private javax.swing.JTextField fileNameTextField;
    private javax.swing.JButton selectFileButton;
    private javax.swing.JTextPane detailsTextPane;
    private javax.swing.JPanel filePanel;
    private javax.swing.JPanel detailsPanel;
    private JScrollPane detailsScrollPane;
    private JScrollPane correlationMatrixScrollPane;
    
    private String[][] semanticTopics;
    
    private final String SEPARATOR = System.getProperty("line.separator");
	
    public CorrelationMatrixViewer() {
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
        correlationMatrixScrollPane = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        detailsScrollPane = new javax.swing.JScrollPane();
        detailsTextPane = new javax.swing.JTextPane();

        setTitle("Correlation Matrix Viewer");
        setToolTipText("correlationView");
        setResizable(true);
        setMaximizable(true);
        
        filePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Matrix File Selection "));
        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Cluster Details "));
        correlationMatrixScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), " Correlation Matrix View "));

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
                .addComponent(correlationMatrixScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(correlationMatrixScrollPane)
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
            	String idsFileName = (selectedFile.getName().contains("clustered") ?
            			selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('-')) : 
            			selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('.'))) + ".ids";
            	
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	DoubleMatrix2D correlationMatrix = new DoubleMatrix2D(selectedFile.getAbsolutePath());
            	CorrelationMatrix matrix = new CorrelationMatrix(documentIds, correlationMatrix);
            	
            	CorrelationMatrixGraphicPanel graphicPanel = null;
            	
            	boolean isClusteredMatrix = selectedFile.getName().contains("clustered");
            	this.detailsPanel.setVisible(isClusteredMatrix);
            	if (isClusteredMatrix) {
            		String projectName = selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('-'));
            		String clustersFileName = projectName + ".clusters";
            		String topicsFileName = projectName + ".topics";
            		
            		int[][] clusters = FileUtilities.readClustering(clustersFileName);
            		this.semanticTopics = FileUtilities.readSemanticTopics(topicsFileName);
            		graphicPanel = new ClusteredCorrelationMatrixGraphicPanel(matrix, clusters);
            		((ClusteredCorrelationMatrixGraphicPanel) graphicPanel).addListener(this);
            	} else 
            		graphicPanel = new CorrelationMatrixGraphicPanel(matrix);
            	
                this.correlationMatrixScrollPane.setViewportView(graphicPanel);
                this.correlationMatrixScrollPane.revalidate();
                this.correlationMatrixScrollPane.repaint();
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
		this.correlationMatrixScrollPane.setViewportView(new JPanel());
		this.correlationMatrixScrollPane.repaint();
	}
	
	@Override
	public void actionPerformed(Integer clusterIndex) {
		String output = " === Cluster " + clusterIndex + " === " + SEPARATOR;
		for (String topic : this.semanticTopics[clusterIndex])
			output += topic + SEPARATOR;
		this.detailsTextPane.setText(output);
	}
}