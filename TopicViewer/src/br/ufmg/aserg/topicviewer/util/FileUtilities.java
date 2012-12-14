package br.ufmg.aserg.topicviewer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.splabs.vocabulary.iR.info.RetrievedInfoIF;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class FileUtilities {
	
	private static final String SEPARATOR = System.getProperty("line.separator");
	private static final String VALUE_SEPARATOR = " ";
	private static final String ENTITY_SEPARATOR = ";";
	
	// ------------------------------------ Write Operations ------------------------------------
	
	public static void saveTermDocumentInfo(RetrievedInfoIF retrievedInfo, String resultFileName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getSortedEntrySet(retrievedInfo.getAllTermIdsMap().entrySet()));
		buffer.append(getSortedEntrySet(retrievedInfo.getAllDocumentIdsMap().entrySet()));
		saveBuffer(buffer, resultFileName);
	}
	
	private static String getSortedEntrySet(Set<Entry<String, Integer>> entrySet) {
		StringBuffer buffer = new StringBuffer();
		
		List<Entry<String, Integer>> setList = new LinkedList<Entry<String, Integer>>(entrySet);
		Collections.sort(setList, getEntrySetComparator());
		
		for (Entry<String, Integer> term : setList)
			buffer.append(term.getKey() + ENTITY_SEPARATOR);
		buffer.append(SEPARATOR);
		
		return buffer.toString();
	}
	
	public static void saveMatrix(DoubleMatrix2D matrix, String resultFileName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(matrix.rows() + " " + matrix.columns() + SEPARATOR);
		buffer.append(getMatrixAsString(matrix));
		saveBuffer(buffer, resultFileName);
	}
	
	public static void copyFile(String fileName, String copyFileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		String str;
		StringBuffer buffer = new StringBuffer();
		while ((str = reader.readLine()) != null)
			buffer.append(str + SEPARATOR);
		
		reader.close();
		saveBuffer(buffer, copyFileName);
	}
	
	public static void saveClustering(int[][] clusters, String resultFileName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(clusters.length + SEPARATOR);
		for (int i = 0; i < clusters.length; i++) {
			for (int j = 0; j < clusters[i].length; j++)
				buffer.append(clusters[i][j] + " ");
			buffer.append(SEPARATOR);
		}
		saveBuffer(buffer, resultFileName);
	}
	
	public static void saveMapping(Map<Integer, Integer> mapping, String resultFileName) {
		StringBuffer buffer = new StringBuffer();
		for (int key : mapping.keySet())
			buffer.append(key + " " + mapping.get(key) + SEPARATOR);
		saveBuffer(buffer, resultFileName);
	}
	
	public static void saveSemanticTopics(String[][] topics, String resultFileName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(topics.length + SEPARATOR);
		for (int i = 0; i < topics.length; i++) {
			for (int j = 0; j < topics[i].length; j++)
				buffer.append(topics[i][j] + " ");
			buffer.append(SEPARATOR);
		}
		saveBuffer(buffer, resultFileName);
	}
	
	public static void saveResults(List<Map<String, Double>> results, String resultFileName) {
		final String TAB = "\t";
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("Package" + TAB + "CLCOC5" + TAB + "CCP" + TAB 
				+ "LCSC" + TAB + "CCBP" + TAB + "CCoP" + TAB + SEPARATOR);
		
		List<String> packages = new LinkedList<String>(results.get(0).keySet());
		Collections.sort(packages);
		
		for (String packageName : packages) {
			buffer.append(packageName + TAB);
			for (Map<String, Double> result : results)
				buffer.append(result.get(packageName) + TAB);
			buffer.append(SEPARATOR);
		}
		
		saveBuffer(buffer, resultFileName);
	}
	
	private static String getMatrixAsString(DoubleMatrix2D matrix) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < matrix.rows(); i++) {
			DoubleMatrix1D row = matrix.viewRow(i);
			for (int j = 0; j < row.size(); j++)
				buffer.append(row.get(j) + " ");
			buffer.append(SEPARATOR);
		}
		
		return buffer.toString();
	}
	
	private static Comparator<Entry<String, Integer>> getEntrySetComparator() {
		return new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};
	}
	
	private static void saveBuffer(StringBuffer buffer, String resultFileName) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName));
			writer.write(buffer.toString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	// ------------------------------------ Read Operations ------------------------------------
	
	public static DoubleMatrix2D readMatrix(String fileName) throws IOException {
		DoubleMatrix2D matrix;
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		String[] bounds = reader.readLine().split(VALUE_SEPARATOR);
		int rows = Integer.parseInt(bounds[0]);
		int columns = Integer.parseInt(bounds[1]);
		
		matrix = new SparseDoubleMatrix2D(rows, columns);
		
		for (int i = 0; i < rows; i++) {
			String[] row = reader.readLine().split(VALUE_SEPARATOR);
			for (int j = 0; j < columns; j++)
				matrix.set(i, j, Double.parseDouble(row[j]));
		}
		
		return matrix;
	}
	
	public static String[] readTermIds(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		String[] termIds = reader.readLine().split(ENTITY_SEPARATOR);
		reader.close();
		
		return termIds;
	}
	
	public static String[] readDocumentIds(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		reader.readLine(); // terms
		String[] documentIds = reader.readLine().split(ENTITY_SEPARATOR);
		reader.close();
		
		return documentIds;
	}
	
	public static int[][] readClustering(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		int numClusters = Integer.parseInt(reader.readLine());
		int[][] clusters = new int[numClusters][0];
		for (int i = 0; i < numClusters; i++) {
			String[] values = reader.readLine().split(VALUE_SEPARATOR);
			int[] cluster = new int[values.length];
			
			for (int j = 0; j < cluster.length; j++)
				cluster[j] = Integer.parseInt(values[j]);
			clusters[i] = cluster;
		}
		
		reader.close();
		return clusters;
	}
	
	public static Map<Integer, Integer> readMapping(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();
		
		String str;
		while ((str = reader.readLine()) != null) {
			String[] map = str.split(VALUE_SEPARATOR);
			mapping.put(Integer.parseInt(map[0]), Integer.parseInt(map[1]));
		}
		
		reader.close();
		return mapping;
	}
	
	public static String[][] readSemanticTopics(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		int numClusters = Integer.parseInt(reader.readLine());
		String[][] clusters = new String[numClusters][0];
		for (int i = 0; i < numClusters; i++)
			clusters[i] = reader.readLine().split(VALUE_SEPARATOR);
		
		reader.close();
		return clusters;
	}
}