package br.ufmg.aserg.topicviewer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Properties {

	private static final String PROPERTIES_FILE = "project.properties";

	private static InputStream propertiesInputStream = null;
	private static java.util.Properties prop = new java.util.Properties();

	// File Properties
	public static final String WORKSPACE = "workspace";
	
	// General Properties
	public static final String VOCABULARY_OUTPUT = "vocabulary";
	public static final String TERM_DOC_MATRIX_OUTPUT = "lsimodel";
	
	public static void load() {
		try {
			File propertiesFile = new File(PROPERTIES_FILE);
			propertiesInputStream = new FileInputStream(propertiesFile);
			if (prop.isEmpty() && propertiesFile.exists()) {
				prop.load(propertiesInputStream);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (propertiesInputStream != null)
					propertiesInputStream.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static String getProperty(String property) {
		String value = prop.getProperty(property);
		return value != null ? value.trim() : null;
	}

	public static void setProperty(String property, String value) {
		prop.setProperty(property, value);
		
		try {
			FileOutputStream outputStream = new FileOutputStream(new File(PROPERTIES_FILE));
			prop.store(outputStream, null);
			outputStream.close();
		} catch (IOException e) {
		}
	}
}