package br.ufmg.aserg.topicviewer.control.distribution;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapGraphicPanel;
import br.ufmg.aserg.topicviewer.util.FileUtilities;

public class DistributionMapComparisonController {

	private static List<String> getAllDocumentIds(String[] projects) throws IOException {
		List<String> allDocumentIds = new LinkedList<String>();
		
		for (String projectName : projects) {
			String[] documentIds = FileUtilities.readDocumentIds(projectName + ".ids");
			for (String documentId : documentIds)
				if (!allDocumentIds.contains(documentId))
					allDocumentIds.add(documentId);
		}
		
		return allDocumentIds;
	}
	
	private static int getDocumentIndex(String documentId, String[] documentIds) {
		for (int i = 0; i < documentIds.length; i++)
			if (documentId.equals(documentIds[i])) return i;
		return -1;
	}
	
	private static int getClusterIndex(int documentId, int[][] clusters) {
		for (int i = 0; i < clusters.length; i++)
			for (int docId : clusters[i])
				if (docId == documentId) return i;
		return -1;
	}
	
	public static void main(String[] args) throws IOException {
		
		String[] projects = new String[args.length];
		for (int i = 0; i < args.length; i++)
			projects[i] = args[i].substring(0, args[i].lastIndexOf('.'));
		
		List<String> allClassNames = getAllDocumentIds(projects);
		System.out.println("Merged all document ids");
		
		for (String project : projects) {
			DistributionMap distributionMap = new DistributionMap(project + "-merge");
			String[] documentIds = FileUtilities.readDocumentIds(project + ".ids");
			int[][] clusters = FileUtilities.readClustering(project + ".clusters");
			
			for (String documentId : allClassNames) {
				String packageName = documentId.substring(documentId.lastIndexOf(':')+1, documentId.lastIndexOf('.'));
				String className = documentId.substring(documentId.lastIndexOf('.')+1);
				
				int documentIndex = getDocumentIndex(documentId, documentIds);
				int cluster = (documentIndex != -1) ? getClusterIndex(documentIndex, clusters) : -1;
				distributionMap.put(packageName, className, cluster);
			}
			
        	String[][] semanticTopics = FileUtilities.readSemanticTopics(project + ".topics");
        	new DistributionMapGraphicPanel(distributionMap, semanticTopics);
        	System.out.println("Merged Project: " + project);
		}
	}
}