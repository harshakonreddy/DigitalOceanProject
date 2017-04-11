package com.digitalocean.packageindex.business;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test class is for unit testing PackageIndexer class
 */
public class PackageIndexerTest {

	private static PackageIndexer pi = null;
	
	@Before
	public void initializeTestData() {
		pi = new PackageIndexer();
		pi.indexPackage("A",null);
	}
	
	@Test
	public void indexPackage_Success() {
		
		// Test case 1
		String packageName = "A";
		List<String> dependencies = null; 
		Assert.assertEquals(pi.indexPackage(packageName, dependencies),"OK");
	}
	
	@Test
	public void queryPackage_Success() {
	
		// Test case 1
		String packageName = "A";
		Assert.assertEquals(pi.queryPackage(packageName),"OK");
	}
	
	@Test
	public void removePackageFromIndex_Success() {
		// Test case 1
		String packageName = "A";
		Assert.assertEquals(pi.queryPackage(packageName),"OK");
	}
	
}
