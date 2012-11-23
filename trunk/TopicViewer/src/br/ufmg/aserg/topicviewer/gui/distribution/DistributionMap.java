package br.ufmg.aserg.topicviewer.gui.distribution;

import java.util.List;
import java.util.Map;

public class DistributionMap {
	
	/*
	 *  o que preciso pro distribution map?
	 *  para cada classe, extrair o pacote e guardar em um Map - ids
	 *  para cada classe, procurar em que cluster ela está - clusterids
	 *  ordenar pacotes por tamanho
	 *  ordenar classes do pacote por cor
	 */
	
	private Map<String, List<String>> packageMapping;
	private Map<String, Integer> classMapping;
	
	public DistributionMap() {
		
	}
	
	public void put(String packageName, String className, int clusterIndex) {
		
	}
	
	public void organize() {
		
	}
}
