package br.ufmg.aserg.topicviewer.control.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.splabs.vocabulary.filter.FilterProperties;
import org.splabs.vocabulary.filter.IdentifierFilter;
import org.splabs.vocabulary.iR.IR;
import org.splabs.vocabulary.iR.IRPropertyKeys;
import org.splabs.vocabulary.iR.info.IRInfo;
import org.splabs.vocabulary.vxl.VXLReader;
import org.splabs.vocabulary.vxl.browsers.DirectoriesBrowser;
import org.splabs.vocabulary.vxl.iterator.VXLIterator;
import org.splabs.vocabulary.vxl.iterator.javamodel.ContainerEntity;
import org.splabs.vocabulary.vxl.iterator.util.VXLReaderPropertyKeys;
import org.splabs.vocabulary.vxl.util.LOCManager;
import org.splabs.vocabulary.vxl.util.VxlManager;
import org.splabs.vocabulary.vxl.vloccount.LOCParameters;

import ptstemmer.exceptions.PTStemmerException;
import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class JavaVocabularyExtractionController extends AbstractController {
	
	private File[] projects;
	private String vocabularyResultFolder;
	private String termDocResultFolder;
	
	private VXLReader vxlReader;
	private IdentifierFilter identifierFilter;
	private IRInfo retrievedInfo;
	
	private java.util.Properties props;
	
	public JavaVocabularyExtractionController(File[] projects, boolean includeComment, boolean includeJavaDoc, boolean removeStopWords, boolean removeSmallWords, int minimalTermLength) {
		super();
		checkDefaultProperties();
		
		this.projects = projects;
		this.setNumStages(this.projects.length*2);
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(VXLReaderPropertyKeys.INCLUDE_COMMENTS, String.valueOf(includeComment));
		properties.put(VXLReaderPropertyKeys.INCLUDE_JAVADOC, String.valueOf(includeJavaDoc));
		properties.put(FilterProperties.STOPWORDS, removeStopWords ? "yes" : "no");
		properties.put(FilterProperties.LIMIT_TERM_LENGTH, removeSmallWords ? new Integer(minimalTermLength).toString() : new Integer(0).toString());
		Properties.setProperties(properties);
		
		this.vocabularyResultFolder = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.VOCABULARY_OUTPUT;
		this.termDocResultFolder = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT;
		checkResultFolder(this.vocabularyResultFolder);
		checkResultFolder(this.termDocResultFolder);
		
		props = Properties.getProperties();
		this.vxlReader = new VXLReader(props);
		this.identifierFilter = new IdentifierFilter(props);
	}
	
	private void loadVXLFile(String vxlFileName) {
		try {
			vxlReader.load(vxlFileName);
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("loadVxlFile(): JAXBException Error");
		}
	}
	
	private void createIRInfoTermsPerEntity() throws PTStemmerException {
		Map<String,List<String>> termsEntitiesMap = new HashMap<String, List<String>>(); 
		
		VXLIterator iterator = vxlReader.iterator();
		while (iterator.hasNext()) {
			ContainerEntity container = iterator.next();
			
			List<String> currentEntityTerms = new ArrayList<String>();
			for (String identifier : container.getListIdentifiers()) {
				for (String term : identifierFilter.filterIdentifiers(new String[] {identifier})) {
					currentEntityTerms.add(term);
				}
			}
			
			termsEntitiesMap.put(container.getEntityIdentifier(), currentEntityTerms);
		}
		
		IR ir = new IR(termsEntitiesMap, props);
		retrievedInfo = new IRInfo(ir.calculate(), props);
	}
	
	@Override
	public void run() {
		for (File project : this.projects) {
			String projectName = project.getName();
			
			try {
			// -------------------------------------- Vocabulary Extraction --------------------------------------
				this.setProgressMessage("Extracting vocabulary from " + projectName + " project");
				
				LOCManager.locParameters = new LinkedList<LOCParameters>();
				DirectoriesBrowser.browse(project.getAbsolutePath(), projectName, "");
				
				String vocabularyFile = this.vocabularyResultFolder + File.separator + projectName + ".vxl";
				checkExistingFile(vocabularyFile);
				VxlManager.save(vocabularyFile);
				
				this.setProgressMessage("Vocabulary from " + projectName + " extracted successfully");
				this.addCompletedStage();
			
			// -------------------------------------- Term Document Indexing --------------------------------------
				this.setProgressMessage("Indexing terms and documents from " + projectName + " project");
				
				this.loadVXLFile(vocabularyFile);
				this.createIRInfoTermsPerEntity();
				
				FileUtilities.saveTermDocumentInfo(this.retrievedInfo, this.termDocResultFolder + File.separator + projectName + ".ids");
				new DoubleMatrix2D(this.retrievedInfo.getTermDocumentMatrix()).save(this.termDocResultFolder + File.separator + projectName + ".matrix");
				
				this.setProgressMessage("Term-Document Matrix from " + projectName + " saved successfully");
				this.addCompletedStage();
			} catch (Exception e) {
				this.failedProjects.add(project);
				e.printStackTrace();
				
				this.addCompletedStage(); this.addCompletedStage();
			}
		}
		
		this.setProgressMessage("Finished");
	}
	
	// evitar append no arquivo
	private void checkExistingFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) file.delete();
	}
	
	private void checkDefaultProperties() {
		if (!Properties.containsProperty(VXLReaderPropertyKeys.CONTAINER_TYPE)) {
			Map<String, String> properties = new HashMap<String, String>();
			// vxl reader
			properties.put(VXLReaderPropertyKeys.CONTAINER_TYPE, VXLReaderPropertyKeys.ContainerType.CLASS.toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_CLASS, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_INTERFACE, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_SUPERCLASS, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_ENUM, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_ATTRIBUTE, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_METHOD, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_PARAMETER, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_LOCAL_VARIABLE, new Boolean(false).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_COMMENTS, new Boolean(true).toString());
			properties.put(VXLReaderPropertyKeys.INCLUDE_JAVADOC, new Boolean(true).toString());
			// identifier filter
			final String YES = "yes";
			properties.put(FilterProperties.ENGLISH_LANGUAGE, YES);
			properties.put(FilterProperties.UNDERSCORE, YES);
			properties.put(FilterProperties.CAMEL_CASE, YES);
			properties.put(FilterProperties.STOPWORDS, YES);
			properties.put(FilterProperties.STEMMING, YES);
			properties.put(FilterProperties.CONVERT_TO_LOWER_CASE, YES);
			properties.put(FilterProperties.LIMIT_TERM_LENGTH, new Integer(3).toString());
			properties.put(FilterProperties.STOPWORDS_FILE, Properties.STOPWORDS_FILE);
			// ir vector model
			properties.put(IRPropertyKeys.DISTANCE_FUNCTION, IRPropertyKeys.DistanceFunctionType.COSINE.toString());
			
			Properties.setProperties(properties);
		}
	}
}