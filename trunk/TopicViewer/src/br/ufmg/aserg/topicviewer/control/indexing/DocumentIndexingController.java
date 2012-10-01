package br.ufmg.aserg.topicviewer.control.indexing;

import java.io.File;

import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.Properties;

public class DocumentIndexingController extends AbstractController {
	
	private File[] projects;
	private String resultFolderName;
	
	public DocumentIndexingController(File[] vocabularyFiles) {
		super();
		
		this.projects = vocabularyFiles;
		this.setAllProjectCount(this.projects.length);
		
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT;
		checkResultFolder(this.resultFolderName);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}