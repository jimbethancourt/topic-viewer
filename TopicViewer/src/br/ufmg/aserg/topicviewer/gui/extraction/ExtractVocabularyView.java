package br.ufmg.aserg.topicviewer.gui.extraction;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

import br.ufmg.aserg.topicviewer.extraction.VocabularyExtractor;

public class ExtractVocabularyView extends javax.swing.JInternalFrame {

	private static final long serialVersionUID = 5793200312207254298L;
	
    private javax.swing.JButton startExtractionButton;
    private javax.swing.JList projectList;
    private javax.swing.JButton selectProjectsButton;
    private javax.swing.JLabel projectSelectionLabel;
    private javax.swing.JPanel projectSelectionPanel;
    private javax.swing.JScrollPane projectScrollPane;
    private javax.swing.JProgressBar extractionProgressBar;
	
	private Thread progressMonitorThread;
    private File[] sourceCodePaths;
    
    private VocabularyExtractor vocabularyExtractor;

    public ExtractVocabularyView() {
        initComponents();
        initListeners();
        
        this.vocabularyExtractor = new VocabularyExtractor();
        this.pack();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initComponents() {

        projectSelectionPanel = new javax.swing.JPanel();
        projectSelectionLabel = new javax.swing.JLabel();
        selectProjectsButton = new javax.swing.JButton();
        projectScrollPane = new javax.swing.JScrollPane();
        projectList = new javax.swing.JList();
        extractionProgressBar = new javax.swing.JProgressBar();
        startExtractionButton = new javax.swing.JButton();

        setClosable(true);
        setTitle("Vocabulary Extraction");
        setName("extractVocabulary");
        setVisible(true);

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

        extractionProgressBar.setStringPainted(true);

        startExtractionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../../img/start.png")));
        startExtractionButton.setText("Start");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(extractionProgressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(projectSelectionPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startExtractionButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startExtractionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(extractionProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    	
    	startExtractionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btStartActionPerformed(evt);
            }
        });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectProjectsActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
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
	private void btStartActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (sourceCodePaths != null) {
            if (!startExtractionButton.getText().trim().isEmpty()) {
//            	this.projectList.get
//                this.controller = new MetricsController(new File(pathSaida.getText().trim()), sourceCodePaths);
//                this.progressMonitorThread = new Thread(this.controller);
//                startExtractionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/wait.gif")));
//                startExtractionButton.setText("");
//                this.repaint();
//                this.pack();
//                this.progressMonitorThread.start();
//                new Thread(new PanelUpdater()).start();
            } else {
                startExtractionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../../img/start.png")));
                startExtractionButton.setText("Start");
                startExtractionButton.repaint();
                this.progressMonitorThread.stop();
            }
        }
    }                                       

    private class PanelUpdater implements Runnable {

        private double value;

        @Override
        public void run() {

//            while (progressMonitorThread != null && progressMonitorThread.isAlive()) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ExtractVocabularyView.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                value = ((double) controller.getAnalisados() / controller.getNumProjetos()) * 100;
//                extractionProgressBar.setValue((int) value);
//                extractionProgressBar.repaint();
//            }
//            
//            if (!controller.getErros().isEmpty()) {
//                StringBuilder sb = new StringBuilder();
//                for (File f : controller.getErros()) {
//                    sb.append("* ").append(f.getName());
//                    sb.append("\n");
//                }
//                JOptionPane.showMessageDialog(null, "Erro ocorrido nos seguintes projetos:\n" + sb.toString() + "Favor verificar os logs!", "Erro", JOptionPane.ERROR_MESSAGE);
//            }
            startExtractionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/start.png")));
            startExtractionButton.setText("Start");
        }
    }
}