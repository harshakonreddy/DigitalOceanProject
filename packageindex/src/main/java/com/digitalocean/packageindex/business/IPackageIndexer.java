package com.digitalocean.packageindex.business;

import java.util.List;

/**
 * The Interface IPackageIndexer provides methods to maintain the index
 */
public interface IPackageIndexer {

	/**
	 * Index a package and maintain its dependencies.
	 *
	 * @param aPackage
	 *            is the package name
	 * @param dependencies
	 *            for the package
	 * @return OK if indexing of package is successful, FAIL otherwise.
	 */
	public String indexPackage(String aPackage, List<String> dependencies);

	/**
	 * Query a package in index.
	 *
	 * @param aPackage
	 *            is the package name
	 * @return OK if package is present in index, FAIL otherwise.
	 */
	public String queryPackage(String aPackage);

	/**
	 * Remove a package from index and its dependencies.
	 *
	 * @param aPackage
	 *            is the package name
	 * @return OK if package is successfully removed, FAIL otherwise.
	 */
	public String removePackageFromIndex(String aPackage);
}
