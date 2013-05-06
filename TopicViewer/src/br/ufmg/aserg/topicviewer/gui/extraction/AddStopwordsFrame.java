package br.ufmg.aserg.topicviewer.gui.extraction;

import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class AddStopwordsFrame extends JInternalFrame {
	
	private static final long serialVersionUID = -715696946429072911L;
	
	private javax.swing.JButton addStopwordsButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel tipLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea stopwordsTextArea;
    
    public AddStopwordsFrame() {
    	super();
        initComponents();
        initListeners();
        this.pack();
    }

    private void initComponents() {
    	
    	this.setTitle("Add Stopwords");

        jScrollPane1 = new javax.swing.JScrollPane();
        stopwordsTextArea = new javax.swing.JTextArea(5, 20);
        tipLabel = new javax.swing.JLabel("Tip: Add any word, separated by new lines");
        addStopwordsButton = new javax.swing.JButton("Add");
        cancelButton = new javax.swing.JButton("Cancel");

        jScrollPane1.setViewportView(stopwordsTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(tipLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addStopwordsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(tipLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStopwordsButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );
    }
    
    private void initListeners() {
    	addStopwordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStopwordsActionPerformed(evt);
            }
        });
    	
    	cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
    }
    
    private void addStopwordsActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
			FileUtilities.appendFile(this.stopwordsTextArea.getText(), Properties.STOPWORDS_FILE);
			JOptionPane.showMessageDialog(this, "Stopword list successfully updated.", "", JOptionPane.INFORMATION_MESSAGE);
			clean();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Could not modify stopwords file.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
    	clean();
    }
    
    private void clean() {
    	this.stopwordsTextArea.setText("");
    	this.setVisible(false);
    }
}