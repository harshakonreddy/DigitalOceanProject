package com.digitalocean.packageindex.business;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.digitalocean.packageindex.data.PackageIndexDataStore;
import com.digitalocean.packageindex.data.Response;
import com.digitalocean.packageindex.log.PackageIndexLogger;

/**
 * The Class PackageIndexer is responsible for maintaining the index of packages.
 * Exposes methods for index, query and remove.  
 */
public class PackageIndexer implements IPackageIndexer {

	/** Object that stores the packages and their dependencies. */
	private static PackageIndexDataStore<String> indexStore = new PackageIndexDataStore<>();
	private final Logger logger=PackageIndexLogger.LOGGER;
	
	@Override
	public String indexPackage(String aPackage,List<String> dependencies) {
		if(aPackage!=null && !aPackage.trim().isEmpty() && verifyDependencies(dependencies)) {
			/* Synchronize indexing of package and dependencies to 
			 * avoid race condition and inconsistency */
			logger.info("Adding to index store package ::"+aPackage);
			synchronized (indexStore) {
				indexStore.getAllUniquePackages().add(aPackage);
				addDependency(aPackage, dependencies);
			}
			return Response.OK.toString();
		}
		return Response.FAIL.toString();
	}
	
	/**
	 * This method store the dependencies provide for given package
	 * 
	 * @param aPackage is PackageName
	 * @param dependencies is List of dependencies for package
	 */
	private void addDependency(String aPackage,List<String> dependencies) {
		if(aPackage!=null && dependencies!=null) {
			indexStore.getIndexDependencies().put(aPackage, dependencies);
		}
	}

	@Override
	public String queryPackage(String aPackage) {
		return indexStore.getAllUniquePackages().contains(aPackage)?Response.OK.toString():Response.FAIL.toString();
	}
	
	@Override
	public String removePackageFromIndex(String aPackage) {
		if(!isPackageADependency(aPackage)) {
			/* Synchronize removing of package and dependencies to 
			 * avoid race condition and inconsistency */
			logger.info("Removing from index store package ::"+aPackage);
			synchronized (indexStore) {
				indexStore.getAllUniquePackages().remove(aPackage);
				indexStore.getIndexDependencies().remove(aPackage);
			}
			return Response.OK.toString();
		}
		return Response.FAIL.toString();
	}
	
	/**
	 * Checks if given package is a dependency for any other Package in the index.
	 *
	 * @param aPackage is PackageName
	 * @return true, if is package is a dependency
	 */
	private boolean isPackageADependency(String aPackage) {
		Iterator<List<String>> itrDependencies = indexStore.getIndexDependencies().values().iterator();
		while(itrDependencies.hasNext()) {
			if(itrDependencies.next().contains(aPackage))
				return true;
		}
		return false;
	}

	/**
	 * Checks if each dependency is already indexed.
	 *
	 * @param dependencies
	 * @return true, if each dependency is already indexed
	 */
	private boolean verifyDependencies(List<String> dependencies) {
		if(dependencies == null || dependencies.size() < 1) return true; 
		for (String dependency : dependencies) {
			if(indexStore.getAllUniquePackages().contains(dependency)) 
				continue;
			return false;
		}
		return true;
	}

}
