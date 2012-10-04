package br.ufmg.aserg.topicviewer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.splabs.vocabulary.iR.info.RetrievedInfoIF;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class FileUtilities {
	
	private static final String SEPARATOR = System.getProperty("line.separator");
	private static final String VALUE_SEPARATOR = " ";
	
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
			buffer.append(term.getKey() + " ");
		buffer.append(SEPARATOR);
		
		return buffer.toString();
	}
	
	public static void saveMatrix(DoubleMatrix2D matrix, String resultFileName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(matrix.rows() + " " + matrix.columns() + SEPARATOR);
		buffer.append(getMatrixAsString(matrix));
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
	
	public static String[][] readIds(String fileName) throws IOException {
		String[][] ids = new String[2][0];
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		ids[0] = reader.readLine().split(VALUE_SEPARATOR);
		ids[1] = reader.readLine().split(VALUE_SEPARATOR);
		
		return ids;
	}
}