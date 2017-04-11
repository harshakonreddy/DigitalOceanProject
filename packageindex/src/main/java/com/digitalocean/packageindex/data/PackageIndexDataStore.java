package com.digitalocean.packageindex.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data store for index values
 */
public class PackageIndexDataStore<T> {

	private Map<T, List<T>> indexDependencies = new HashMap<T, List<T>>();

	public Map<T, List<T>> getIndexDependencies() {
		return indexDependencies;
	}

	public void setIndexDependencies(Map<T, List<T>> indexDependencies) {
		this.indexDependencies = indexDependencies;
	}

}
