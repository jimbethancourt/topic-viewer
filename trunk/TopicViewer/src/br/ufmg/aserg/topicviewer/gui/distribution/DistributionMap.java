package br.ufmg.aserg.topicviewer.gui.distribution;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DistributionMap {
	
	private String projectName;
	private Map<String, PackageInfo> packageMapping;
	private Map<String, ClassInfo> classMapping;
	private Map<Integer, TopicInfo> topicMapping;
	
	public DistributionMap(String projectName) {
		this.projectName = projectName;
		this.packageMapping = new HashMap<String, PackageInfo>();
		this.classMapping = new HashMap<String, ClassInfo>();
		this.topicMapping = new HashMap<Integer, TopicInfo>();
	}
	
	public void put(String packageName, String className, int clusterIndex, double ccp) {
		List<String> classes = (this.packageMapping.containsKey(packageName)) ? 
				this.packageMapping.get(packageName).classNames : 
				new LinkedList<String>();
		classes.add(className);
				
		this.packageMapping.put(packageName, new PackageInfo(classes, ccp));
		this.classMapping.put(className, new ClassInfo(clusterIndex));
	}
	
	public void put(int clusterIndex, double spread, double focus) {
		this.topicMapping.put(clusterIndex, new TopicInfo(spread, focus));
	}
	
	public String getProjectName() {
		return this.projectName;
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
						packageMapping.get(package2).classNames.size(), 
						packageMapping.get(package1).classNames.size());
			}
		});
		
		return packages;
	}
	
	public List<String> getClasses(String packageName) {
		return this.packageMapping.get(packageName).classNames;
	}
	
	public Integer getCluster(String className) {
		return this.classMapping.get(className).clusterIndex;
	}
	
	public Double getSpread(int clusterIndex) {
		return this.topicMapping.get(clusterIndex).spread;
	}
	
	public Double getFocus(int clusterIndex) {
		return this.topicMapping.get(clusterIndex).focus;
	}
	
	/**
	 * Sort all class names by the cluster they're in
	 */
	public void organize(boolean byName) {
		for (String packageName : this.packageMapping.keySet()) {
			List<String> classes = this.packageMapping.get(packageName).classNames;
			
			if (byName) {
				Collections.sort(classes);
			} else {
				Collections.sort(classes, new Comparator<String>() {
					@Override
					public int compare(String class1, String class2) {
						return Integer.compare(
								classMapping.get(class1).clusterIndex, 
								classMapping.get(class2).clusterIndex);
					}
				});
			}
		}
	}
	
	static class PackageInfo {
		List<String> classNames;
		double ccp;
		
		PackageInfo(List<String> classNames, double ccp) {
			this.classNames = classNames;
			this.ccp = ccp;
		}
	}
	
	static class ClassInfo {
		int clusterIndex;
		
		ClassInfo(int topicIndex) {
			this.clusterIndex = topicIndex;
		}
	}
	
	static class TopicInfo {
		double spread;
		double focus;
		
		TopicInfo(double spread, double focus) {
			this.spread = spread;
			this.focus = focus;
		}
	}
}