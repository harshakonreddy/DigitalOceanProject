package com.digitalocean.packageindex.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data store for index values
 */
public class PackageIndexDataStore<T> {

	private Set<T> allUniquePackages = new HashSet<T>();
	private Map<T, List<T>> indexDependencies = new HashMap<T, List<T>>();

	public Set<T> getAllUniquePackages() {
		return allUniquePackages;
	}

	public void setAllUniquePackages(Set<T> allUniquePackages) {
		this.allUniquePackages = allUniquePackages;
	}

	public Map<T, List<T>> getIndexDependencies() {
		return indexDependencies;
	}

	public void setIndexDependencies(Map<T, List<T>> indexDependencies) {
		this.indexDependencies = indexDependencies;
	}

}
