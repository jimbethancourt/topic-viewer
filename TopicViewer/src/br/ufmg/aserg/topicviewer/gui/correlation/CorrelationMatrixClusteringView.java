package br.ufmg.aserg.topicviewer.gui.correlation;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JSlider;

import br.ufmg.aserg.topicviewer.control.correlation.clustering.CorrelationMatrixClusteringController;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class CorrelationMatrixClusteringView extends AbstractView {
	
	private static final long serialVersionUID = 7434005933624006244L;
	
    @SuppressWarnings("rawtypes")
	private javax.swing.JList correlationMatrixList;
    private File[] correlationMatrixFiles;
    private javax.swing.JButton matrixSelectionButton;
    private javax.swing.JLabel matrixLabel;
    private javax.swing.JPanel matrixSelectionPanel;
    private javax.swing.JPanel numClusterPanel;
    private javax.swing.JScrollPane matrixScrollPane;
    private javax.swing.JSlider numClusterSlider;

    public CorrelationMatrixClusteringView() {
        initComponents();
        initListeners();
        this.pack();
    }

    @SuppressWarnings("rawtypes")
    private void initComponents() {

        matrixSelectionPanel = new javax.swing.JPanel();
        matrixLabel = new javax.swing.JLabel();
        matrixSelectionButton = new javax.swing.JButton();
        matrixScrollPane = new javax.swing.JScrollPane();
        correlationMatrixList = new javax.swing.JList();
        numClusterPanel = new javax.swing.JPanel();
        numClusterSlider = new javax.swing.JSlider(JSlider.HORIZONTAL, 5, 12, 9);

        setClosable(true);
        setTitle("Extrair Metricas com Moose");
        setName("Extrair Métricas");
        
        matrixSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Correlation Matrix Selection "));
        matrixLabel.setText("Correlation Matrices");
        matrixSelectionButton.setText("Selecionar");
        matrixScrollPane.setViewportView(correlationMatrixList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(matrixSelectionPanel);
        matrixSelectionPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(matrixScrollPane)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(matrixLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                        .addComponent(matrixSelectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(matrixLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(matrixSelectionButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(matrixScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
        );

        numClusterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Number of Clusters "));
        numClusterSlider.setPaintLabels(true);
        numClusterSlider.setPaintTicks(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(numClusterPanel);
        numClusterPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numClusterSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(numClusterSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(matrixSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numClusterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startExecutionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(matrixSelectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numClusterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startExecutionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }
    
    private void initListeners() {
    	matrixSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
    	
    	startExecutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startClusteringActionPerformed(evt);
            }
        });
    }
    
    private int getNumClusters() {
    	return numClusterSlider.getValue();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.WORKSPACE)));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
            Vector vector = new Vector();
            for (File f : chooser.getSelectedFiles())
                vector.addElement(f);
            
            this.correlationMatrixFiles = (File[]) vector.toArray(new File[0]);
            this.correlationMatrixList.setListData(vector);
            this.correlationMatrixList.setSelectionInterval(0, vector.size());
        }
    }                                        

    @SuppressWarnings("deprecation")
	private void startClusteringActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (correlationMatrixFiles != null) {
            if (!startExecutionButton.getText().trim().isEmpty()) {
            	int numClusters = getNumClusters();
            	
                this.controller = new CorrelationMatrixClusteringController(correlationMatrixFiles, numClusters);
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
    	this.correlationMatrixFiles = null;
    	this.correlationMatrixList.setListData(new Vector<File>());
    	this.numClusterSlider.setValue(9);
    	this.progressBar.setValue(0);
    	this.repaint();
    }
}