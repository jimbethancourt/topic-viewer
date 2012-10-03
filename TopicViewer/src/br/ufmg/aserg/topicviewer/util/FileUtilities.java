package br.ufmg.aserg.topicviewer.util;

import java.io.BufferedWriter;
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

public class FileUtilities {
	
	private static final String SEPARATOR = System.getProperty("line.separator");
	
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

//	public static void saveTXTFile(String fileName, ShortenedSparseGraph g) {
//		final String separator = System.getProperty("line.separator");
//		StringBuffer buffer = new StringBuffer();
//		
//		buffer.append(g.getVertices().size() + separator);
//		for (ShortenedVertex v : g.getVertices())
//			buffer.append(v.getIndex() + " " + v.getLabel() + separator);
//		
//		buffer.append(g.getEdges().size() + separator);
//		for (ShortenedUndirectedEdge e : g.getEdges())
//			buffer.append(e.getIndexSrc() + " " + e.getIndexTgt() + " " + e.getStrength() + separator);
//		
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//			writer.write(buffer.toString());
//			writer.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	public static ShortenedSparseGraph readTXTFile(String filename) throws IOException {
//		final String SEPARATOR = " ";
//		ShortenedSparseGraph graph = new ShortenedSparseGraph();
//		BufferedReader reader = new BufferedReader(new FileReader(filename));
//		
//		int numVertices = Integer.parseInt(reader.readLine());
//		for (int i = 0; i < numVertices; i++) {
//			String[] vertex = reader.readLine().split(SEPARATOR);
//			graph.addVertex(new ShortenedVertex(
//					Integer.parseInt(vertex[0]), 
//					vertex[1]));
//		}
//		
//		int numEdges = Integer.parseInt(reader.readLine());
//		for (int i = 0; i < numEdges; i++) {
//			String[] edge = reader.readLine().split(SEPARATOR);
//			graph.addEdge(new ShortenedUndirectedEdge(
//					Integer.parseInt(edge[0]), 
//					Integer.parseInt(edge[1]), 
//					Double.parseDouble(edge[2])));
//		}
//		reader.close();
//		
//		return graph;
//	}
}