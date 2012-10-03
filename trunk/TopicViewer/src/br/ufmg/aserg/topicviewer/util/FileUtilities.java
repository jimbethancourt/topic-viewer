package br.ufmg.aserg.topicviewer.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.splabs.vocabulary.iR.info.RetrievedInfoIF;

public class FileUtilities {
	
	public static void saveTermDocumentMatrix(RetrievedInfoIF retrievedInfo, String resultFileName) {
		
		final String separator = System.getProperty("line.separator");
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(retrievedInfo.getAllTerms().size() + " " + retrievedInfo.getAllDocumentIds().size() + separator);
		
		List<Entry<String, Integer>> allTerms = new LinkedList<Entry<String, Integer>>();
		allTerms.addAll(retrievedInfo.getAllTermIdsMap().entrySet());
		Collections.sort(allTerms, getEntrySetComparator());
		
		// TODO salvar matriz de transformação
		
		
		
		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName));
			writer.write(buffer.toString());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static Comparator<Entry<String, Integer>> getEntrySetComparator() {
		return new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};
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