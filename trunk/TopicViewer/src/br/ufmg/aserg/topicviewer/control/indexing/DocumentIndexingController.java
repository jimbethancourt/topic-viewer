package br.ufmg.aserg.topicviewer.control.indexing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.splabs.vocabulary.filter.FilterProperties;
import org.splabs.vocabulary.filter.IdentifierFilter;
import org.splabs.vocabulary.iR.IR;
import org.splabs.vocabulary.iR.IRPropertyKeys;
import org.splabs.vocabulary.iR.info.LSIInfo;
import org.splabs.vocabulary.vxl.VXLReader;
import org.splabs.vocabulary.vxl.iterator.VXLIterator;
import org.splabs.vocabulary.vxl.iterator.javamodel.ContainerEntity;
import org.splabs.vocabulary.vxl.iterator.util.VXLReaderPropertyKeys;

import ptstemmer.exceptions.PTStemmerException;
import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.DoubleMatrix2D;
import br.ufmg.aserg.topicviewer.util.FileUtilities;
import br.ufmg.aserg.topicviewer.util.Properties;

public class DocumentIndexingController extends AbstractController {
	
	private File[] vocabularyFiles;
	
	private VXLReader vxlReader;
	private IdentifierFilter identifierFilter;
	private LSIInfo retrievedInfo;
	
	private java.util.Properties props;
	
	public DocumentIndexingController(File[] vocabularyFiles, String weightFunction, String tfVariant, Integer lowRank) {
		super();
		checkDefaultProperties();
		
		this.vocabularyFiles = vocabularyFiles;
		this.setAllProjectCount(this.vocabularyFiles.length);
		
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT;
		checkResultFolder();
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(IRPropertyKeys.SCORE_CALCULATOR_TYPE, weightFunction);
		properties.put(IRPropertyKeys.TERM_FREQUENCY_VARIANT_TYPE, tfVariant);
		properties.put(IRPropertyKeys.LSI_LOW_RANK_VALUE, lowRank.toString());
		Properties.setProperties(properties);
		
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
		retrievedInfo = new LSIInfo(ir.calculate(), props);
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
			properties.put(FilterProperties.LIMIT_TERM_LENGTH, new Integer(0).toString());
			// ir vector model
			properties.put(IRPropertyKeys.DISTANCE_FUNCTION, IRPropertyKeys.DistanceFunctionType.EUCLIDEAN.toString());
			
			Properties.setProperties(properties);
		}
	}

	@Override
	public void run() {
		for (File vocabularyFile : this.vocabularyFiles) {
			try {
				String projectName = vocabularyFile.getName().substring(0, vocabularyFile.getName().lastIndexOf('.'));
				
				this.loadVXLFile(vocabularyFile.getAbsolutePath());
				this.createIRInfoTermsPerEntity();
				
				FileUtilities.saveTermDocumentInfo(this.retrievedInfo, this.resultFolderName + File.separator + projectName + ".ids");
				new DoubleMatrix2D(this.retrievedInfo.getTermDocumentMatrix()).save(this.resultFolderName + File.separator + projectName + ".matrix");
				new DoubleMatrix2D(this.retrievedInfo.getLsiTermDocumentMatrix()).save(this.resultFolderName + File.separator + projectName + "-lsi.matrix");
				new DoubleMatrix2D(this.retrievedInfo.getLsiTransformMatrix()).save(this.resultFolderName + File.separator + projectName + ".lsi");
			} catch (Exception e) {
				this.failedProjects.add(vocabularyFile);
				e.printStackTrace();
			}
			
			this.addAnalyzedProject();
		}
	}
}