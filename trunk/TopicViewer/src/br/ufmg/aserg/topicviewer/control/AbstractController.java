package br.ufmg.aserg.topicviewer.control;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractController implements Runnable {
	
	protected int allProjectCount;
	protected int analyzedProjectsCount;
	protected List<File> failedProjects;
	protected String resultFolderName;
	
	public AbstractController() {
		this.allProjectCount = 0;
		this.analyzedProjectsCount = 0;
		
		this.failedProjects = new LinkedList<File>();
	}
	
	public int getAllProjectCount() {
		return this.allProjectCount;
	}
	
	protected void setAllProjectCount(int projectCount) {
		this.allProjectCount = projectCount;
	}
	
	public int getAnalyzedProjectCount() {
		return this.analyzedProjectsCount;
	}
	
	protected void addAnalyzedProject() {
		this.analyzedProjectsCount++;
	}
	
	public List<File> getFailedProjects() {
		return this.failedProjects;
	}
	
	protected void addFailureProject(File failedProject) {
		this.failedProjects.add(failedProject);
	}
	
	protected void checkResultFolder() {
		File resultFolder = new File(this.resultFolderName);
		if (!resultFolder.exists()) resultFolder.mkdirs();
	}
}