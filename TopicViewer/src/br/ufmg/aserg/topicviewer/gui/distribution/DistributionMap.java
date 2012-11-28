package br.ufmg.aserg.topicviewer.gui.distribution;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DistributionMap {
	
	private Map<String, List<String>> packageMapping;
	private Map<String, Integer> classMapping;
	
	public DistributionMap() {
		this.packageMapping = new HashMap<String, List<String>>();
		this.classMapping = new HashMap<String, Integer>();
	}
	
	public void put(String packageName, String className, int clusterIndex) {
		List<String> classes = (this.packageMapping.containsKey(packageName)) ? 
				this.packageMapping.get(packageName) : 
				new LinkedList<String>();
		classes.add(className);
				
		this.packageMapping.put(packageName, classes);
		this.classMapping.put(className, clusterIndex);
	}
	
	/**
	 * Returns all packages of the distribution map, sorted by package size
	 * @return
	 */
	public List<String> getPackages() {
		List<String> packages = new LinkedList<String>();
		packages.addAll(this.packageMapping.keySet());
		
		Collections.sort(packages, new Comparator<String>() {
			@Override
			public int compare(String package1, String package2) {
				return Integer.compare(
						packageMapping.get(package1).size(), 
						packageMapping.get(package2).size());
			}
		});
		
		return packages;
	}
	
	public List<String> getClasses(String packageName) {
		return this.packageMapping.get(packageName);
	}
	
	public Integer getCluster(String className) {
		return this.classMapping.get(className);
	}
	
	/**
	 * Sort all class names by the cluster they're in
	 */
	public void organize() {
		for (String packageName : this.packageMapping.keySet()) {
			List<String> classes = this.packageMapping.get(packageName);
			
			Collections.sort(classes, new Comparator<String>() {

				@Override
				public int compare(String class1, String class2) {
					return Integer.compare(
							classMapping.get(class1), 
							classMapping.get(class2));
				}
			});
		}
	}
}