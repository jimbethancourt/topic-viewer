package br.ufmg.aserg.topicviewer.gui.measurement;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

import br.ufmg.aserg.topicviewer.control.measurement.ConceptualMetricsCalculatorController;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class ConceptualMetricsCalculatorView extends AbstractView {

	private static final long serialVersionUID = 3366770023407444368L;

    @SuppressWarnings("rawtypes")
	private javax.swing.JList projectList;
    private javax.swing.JButton selectProjectsButton;
    private javax.swing.JLabel projectSelectionLabel;
    private javax.swing.JPanel projectSelectionPanel;
    private javax.swing.JScrollPane projectScrollPane;
	
    private File[] sourceCodePaths;
    
    public ConceptualMetricsCalculatorView() {
    	super();
        initComponents();
        initListeners();
        this.pack();
    }

    @SuppressWarnings("rawtypes")
    private void initComponents() {

        projectSelectionPanel = new javax.swing.JPanel();
        projectSelectionLabel = new javax.swing.JLabel();
        selectProjectsButton = new javax.swing.JButton();
        projectScrollPane = new javax.swing.JScrollPane();
        projectList = new javax.swing.JList();

        setTitle("Conceptual Metrics Calculator");
        setName("conceptualMetrics");

        projectSelectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Project Selection "));
        projectSelectionLabel.setText("Projects");
        selectProjectsButton.setText("Select");

        projectScrollPane.setViewportView(projectList);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(projectSelectionPanel);
        projectSelectionPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(projectScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(projectSelectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectProjectsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectSelectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectProjectsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addContainerGap())
        );

        progressBar.setStringPainted(true);

        startExecutionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../../img/start.png")));
        startExecutionButton.setText("Start");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(projectSelectionPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startExecutionButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startExecutionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }
    
    private void initListeners() {
    	selectProjectsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectProjectsActionPerformed(evt);
            }
        });
    	
    	startExecutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startExtractionActionPerformed(evt);
            }
        });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectProjectsActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
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
            Vector vector = new Vector();
            for (File f : chooser.getSelectedFiles())
                vector.addElement(f);
            
            this.sourceCodePaths = (File[]) vector.toArray(new File[0]);
            this.projectList.setListData(vector);
            this.projectList.setSelectionInterval(0, vector.size());
        }
    }                                        

    @SuppressWarnings("deprecation")
	private void startExtractionActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (sourceCodePaths != null) {
            if (!startExecutionButton.getText().trim().isEmpty()) {
                this.controller = new ConceptualMetricsCalculatorController(sourceCodePaths);
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
    	this.sourceCodePaths = null;
    	this.projectList.setListData(new Vector<File>());
    	this.progressBar.setValue(0);
    	this.repaint();
    }
}