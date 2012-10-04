package br.ufmg.aserg.topicviewer.control.correlation;

import java.io.File;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.Properties;

public class CorrelationMatrixCalculator extends AbstractController {
	
	private File matrixFile;
	private File idsFile;
	
	public CorrelationMatrixCalculator(File matrixFile) {
		super();
		
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.CORRELATION_MATRIX_OUTPUT;
		this.checkResultFolder();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}