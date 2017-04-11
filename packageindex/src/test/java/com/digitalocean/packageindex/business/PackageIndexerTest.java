package com.digitalocean.packageindex.business;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.digitalocean.packageindex.data.Response;

/**
 * This test class is for unit testing PackageIndexer class
 */
public class PackageIndexerTest {

	private static PackageIndexer pi = null;
	
	@BeforeClass
	public void initializeTestData() {
		pi = new PackageIndexer();
	}
	
	@Test
	public void indexPackage_Success() {
		
		// Index with empty dependencies
		String packageName = "X1";
		List<String> dependencies = null; 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),Response.OK.toString());
		
		packageName = "X2";
		dependencies = null; 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),Response.OK.toString());
		
		// Index with dependencies already indexed
		packageName = "X";
		dependencies = Arrays.asList("X1"); 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),Response.OK.toString());
		
		packageName = "Y";
		dependencies = Arrays.asList("X1"); 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),Response.OK.toString());
		
		// Index with new dependencies
		packageName = "Y";
		dependencies = Arrays.asList("X1","X2"); 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),Response.OK.toString());
	}
	
	@Test
	public void indexPackage_Failure() {
	
		// Indexing package with dependencies that are not indexed yet
		String packageName = "XYZ";
		List<String> dependencies = Arrays.asList("Z"); 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),Response.FAIL.toString());
	}
	
	@Test (dependsOnMethods = {"indexPackage_Success"})
	public void queryPackage_Success() {
	
		// Query for packages with no dependencies
		String packageName = "X1";
		Assert.assertEquals(pi.queryPackage(packageName),Response.OK.toString());
		
		// Query for packages with dependencies		
		packageName = "X";
		Assert.assertEquals(pi.queryPackage(packageName),Response.OK.toString());
	}
	
	@Test (dependsOnMethods = {"indexPackage_Success"})
	public void queryPackage_Failure() {
	
		// Query for packages with no dependencies
		String packageName = "Z";
		Assert.assertEquals(pi.queryPackage(packageName),Response.FAIL.toString());
		
	}
	
	@Test (dependsOnMethods = {"indexPackage_Success"})
	public void removePackageFromIndex_Success() {
		// Remove package that is indexed
		String packageName = "Y";
		Assert.assertEquals(pi.removePackageFromIndex(packageName),Response.OK.toString());
		
		// Remove package that is not indexed
		packageName = "Z";
		Assert.assertEquals(pi.removePackageFromIndex(packageName),Response.OK.toString());
	}
	
	@Test (dependsOnMethods = {"indexPackage_Success"})
	public void removePackageFromIndex_Failure() {
		// Remove package that is indexed but is a dependency
		String packageName = "X1";
		Assert.assertEquals(pi.removePackageFromIndex(packageName),Response.FAIL.toString());
		
	}
	
}
