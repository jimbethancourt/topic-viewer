package br.ufmg.aserg.topicviewer.gui.correlation;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.ufmg.aserg.topicviewer.control.correlation.CorrelationMatrix;
import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;
import cern.colt.matrix.DoubleMatrix2D;

public class CorrelationMatrixViewer extends AbstractView {
	
	private static final long serialVersionUID = -5260758202187415775L;
	
	private javax.swing.JTextField fileNameTextField;
    private javax.swing.JButton selectFileButton;
    private javax.swing.JPanel filePanel;
    private JScrollPane correlationMatrixScrollPane;
	
    public CorrelationMatrixViewer() {
    	super();
        initComponents();
        initListeners();
        this.pack();
    }

    private void initComponents() {

        filePanel = new javax.swing.JPanel();
        fileNameTextField = new javax.swing.JTextField();
        selectFileButton = new javax.swing.JButton();
        correlationMatrixScrollPane = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        setTitle("Correlation Matrix Viewer");
        setToolTipText("correlationView");
        setResizable(true);
        setMaximizable(true);
        
        filePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Matrix File Selection ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 12))); // NOI18N
        correlationMatrixScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), " Correlation Matrix View "));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(correlationMatrixScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 505, Short.MAX_VALUE)
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
                        .addGap(0, 321, Short.MAX_VALUE)))
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
            	String idsFileName = selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().lastIndexOf('.')) + ".ids";
            	
            	String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
            	DoubleMatrix2D correlationMatrix = FileUtilities.readMatrix(selectedFile.getAbsolutePath());
            	
            	CorrelationMatrix matrix = new CorrelationMatrix(documentIds, correlationMatrix);
            	
                this.correlationMatrixScrollPane.setViewportView(new CorrelationMatrixGraphicPanel(matrix));
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
		this.correlationMatrixScrollPane.setViewportView(new JPanel());
		this.correlationMatrixScrollPane.repaint();
	}
}