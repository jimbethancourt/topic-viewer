package br.ufmg.aserg.topicviewer.gui.correlation;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import br.ufmg.aserg.topicviewer.gui.AbstractView;
import br.ufmg.aserg.topicviewer.util.Properties;

public class CorrelationMatrixView extends AbstractView {
	
	private static final long serialVersionUID = -5260758202187415775L;
	
	private javax.swing.JTextField fileNameTextField;
    private javax.swing.JButton selectFileButton;
    private javax.swing.JPanel filePanel;
    private javax.swing.JPanel correlationMatrixPanel;
	
    public CorrelationMatrixView() {
    	super();
        initComponents();
        initListeners();
        this.pack();
    }

    private void initComponents() {

        filePanel = new javax.swing.JPanel();
        fileNameTextField = new javax.swing.JTextField();
        selectFileButton = new javax.swing.JButton();
        correlationMatrixPanel = new javax.swing.JPanel();

        setTitle("Correlation Matrix Viewer");
        setToolTipText("correlationMatrix");
        setResizable(true);
        setMaximizable(true);
        
        filePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Matrix File Selection ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 12))); // NOI18N
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

        correlationMatrixPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), " Correlation Matrix ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 12))); // NOI18N
        correlationMatrixPanel.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(correlationMatrixPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(correlationMatrixPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 317, Short.MAX_VALUE)))
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
            this.fileNameTextField.setText(chooser.getSelectedFile().getAbsolutePath());
//            try {
//                Object[] arestasVerticesFromCSV = FileController.getArestasVerticesFromCSV(chooser.getSelectedFile());
//                List<String> vertices = (List<String>) arestasVerticesFromCSV[0];
//                List<String[]> arestas = (List<String[]>) arestasVerticesFromCSV[1];
//                controller = new GraphController(vertices, arestas);
//                directedGraph = controller.getDirectedGraph(this.correlationMatrixPanel.getSize());
//                directedGraph.setEditable(false);
//                Object[] allMaximalCliques = controller.getAllMaximalCliques().toArray();
//                Arrays.sort(allMaximalCliques, new Comparator() {
//
//                    @Override
//                    public int compare(Object o1, Object o2) {
//                        Set object1 = (Set) o1;
//                        Set object2 = (Set) o2;
//                        if (object1.size() == object2.size()) {
//                            return 0;
//                        } else if (object1.size() > object2.size()) {
//                            return -1;
//                        } else {
//                            return 1;
//                        }
//                    }
//                });
//                this.listCliques.setListData(allMaximalCliques);
//                this.correlationMatrixPanel.removeAll();
                JScrollPane jsp = new JScrollPane(new CorrelationMatrixGraphicPanel(null));
                jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                this.correlationMatrixPanel.removeAll();
                this.correlationMatrixPanel.add(jsp);
                this.correlationMatrixPanel.revalidate();
                this.correlationMatrixPanel.repaint();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }
    }                                        

	@Override
	public void refresh() {
		this.fileNameTextField.setText("");
		this.correlationMatrixPanel.removeAll();
		this.correlationMatrixPanel.repaint();
	}
}