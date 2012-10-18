package br.ufmg.aserg.topicviewer.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateClusterFile {

	public static void main(String[] args) {
		Properties.load();
		
		final char STRING_SEPARATOR = ':';
		final String SEPARATOR = System.getProperty("line.separator");
		final String[] projects = {"Source-7_3_1", "Source-7_4_1", "Source-7_5_1", "Source-7_6"};
		final String correlationFolder = Properties.getProperty(Properties.WORKSPACE) + File.separator + Properties.CORRELATION_MATRIX_OUTPUT + File.separator;
		
		for (String project : projects) {
			String idsFileName = correlationFolder + project + ".ids";
			String clustersFileName = correlationFolder + project + ".clusters";
			String resultFileName = correlationFolder + project + ".clustersIds";
					
			try {
				String[] documentIds = FileUtilities.readDocumentIds(idsFileName);
				int[][] clusters = FileUtilities.readClustering(clustersFileName);
				
				StringBuffer buffer = new StringBuffer();
				buffer.append(clusters.length + SEPARATOR);
				for (int[] cluster : clusters) {
					for (int documentId : cluster) {
						String document = documentIds[documentId];
						buffer.append(document.substring(document.lastIndexOf(STRING_SEPARATOR)+1) + " ");
					}
					buffer.append(SEPARATOR); buffer.append(SEPARATOR);
				}

				BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName));
				writer.write(buffer.toString());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
