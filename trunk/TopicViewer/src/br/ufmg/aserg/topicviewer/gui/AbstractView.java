package br.ufmg.aserg.topicviewer.gui;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import br.ufmg.aserg.topicviewer.control.AbstractController;

public abstract class AbstractView extends JInternalFrame {
	
	private static final long serialVersionUID = 4520698614926883560L;
	
	protected Thread progressMonitorThread;
	protected javax.swing.JProgressBar progressBar;
	protected javax.swing.JButton startExtractionButton;
	
	protected AbstractController controller;

	public AbstractView() {
		setVisible(true);
		setClosable(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	public abstract void refresh();
	
    protected class PanelUpdater implements Runnable {

        private double value;

        public PanelUpdater() {}

		@Override
        public void run() {

            while (progressMonitorThread != null && progressMonitorThread.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
                
                value = ((double) controller.getAnalyzedProjectCount() / controller.getAllProjectCount()) * 100;
                progressBar.setValue((int) value);
                progressBar.repaint();
            }
            
            if (!controller.getFailedProjects().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (File f : controller.getFailedProjects()) {
                    sb.append("* ").append(f.getName());
                    sb.append("\n");
                }
                JOptionPane.showMessageDialog(null, "Projects Failed:\n" + sb.toString() + "\nPlease check log file!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
            startExtractionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("../img/start.png")));
            startExtractionButton.setText("Start");
        }
    }
}