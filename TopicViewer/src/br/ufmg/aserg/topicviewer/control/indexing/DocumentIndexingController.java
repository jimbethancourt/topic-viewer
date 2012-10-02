package br.ufmg.aserg.topicviewer.control.indexing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.splabs.vocabulary.filter.IdentifierFilter;
import org.splabs.vocabulary.vxl.VXLReader;
import org.splabs.vocabulary.vxl.iterator.VXLIterator;
import org.splabs.vocabulary.vxl.iterator.javamodel.ContainerEntity;
import org.splabs.vocabulary.vxl.iterator.util.VXLReaderPropertyKeys;

import ptstemmer.exceptions.PTStemmerException;
import br.ufmg.aserg.topicviewer.control.AbstractController;
import br.ufmg.aserg.topicviewer.util.Properties;

public class DocumentIndexingController extends AbstractController {
	
	private File[] projects;
	private VXLReader vxlReader;
	private IdentifierFilter identifierFilter;
	
	
	private String resultFolderName;
	
	// TODO later: implementar a inserção de vocabulário de superclasse para subclasse, segundo Adrian Kuhn
	
	public DocumentIndexingController(File[] vocabularyFiles) {
		super();
		checkDefaultProperties();
		
		this.projects = vocabularyFiles;
		this.setAllProjectCount(this.projects.length);
		
		this.resultFolderName = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.TERM_DOC_MATRIX_OUTPUT;
		checkResultFolder(this.resultFolderName);
		
		// TODO set properties
	}
	
	public void createIRInfoTermsPerEntity() throws PTStemmerException {
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
		
//		IR ir = new IR(termsEntitiesMap, termsCounterProps);
//		retrievedInfo = new LSIInfo(ir.calculate(), termsCounterProps);
	}
	
	
	/*
# ------ Identifier Filter Properties ------
english=yes
underscore=yes
camelcase=yes
intt=no
stopwords=yes
stemming=yes
orengo=no
convertToLowerCase=yes
limitTermLength=0

# ------ Information Retrieval Properties ------
irFunctionType=IR
tfVariant=ABSOLUTE 
scoreCalculator=TF
distanceFunction=CANBERRA

# ------ Dispersion Measures Properties
dispersionMeasures=no
thresholdHE=10E-6
thresholdHC=10E-6

# ---------- Terms Counter Properties ----------
createMarginals=yes
generateCSV=yes
generateTXT=yes*/
	
	private void checkDefaultProperties() {
		if (!Properties.containsProperty(VXLReaderPropertyKeys.CONTAINER_TYPE)) {
			Map<String, String> properties = new HashMap<String, String>();
			
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
			
			
			Properties.setProperties(properties);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}