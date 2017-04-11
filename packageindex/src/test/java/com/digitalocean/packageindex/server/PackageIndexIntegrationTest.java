package com.digitalocean.packageindex.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.digitalocean.packageindex.business.InputHandler;
import com.digitalocean.packageindex.data.Response;

public class PackageIndexIntegrationTest {

	private Socket clientSocket = null;
	private DataOutputStream indexInput = null;
	private BufferedReader output = null;
	private List<String> success = null;
	private List<String> failure = null;
	private List<String> error = null;
	
	@BeforeClass
	public void startClient() {
		try {
			
			clientSocket = new Socket("localhost", 8080);
			indexInput = new DataOutputStream(clientSocket.getOutputStream());
			output = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
		} catch(Exception e) {
			
		}
		
		success = new ArrayList<String>();
		failure = new ArrayList<String>();
		error = new ArrayList<String>();
		
		// index package with no dependencies
		success.add("INDEX|java|");
		success.add("INDEX|go|");
		success.add("INDEX|ruby|");
		// index package with dependencies
		success.add("INDEX|maven|java");
		success.add("INDEX|testNG|java");
		//update existing dependencies
		success.add("INDEX|maven|java,go");
		
		// query package with no dependency
		success.add("QUERY|go|");
		// query package with dependency
		success.add("QUERY|maven|");
		
		// remove package that is not a dependency and has dependencies
		success.add("REMOVE|maven|");
		// remove package that has no dependency
		success.add("REMOVE|ruby|");
		// remove package is not yet indexed
		success.add("REMOVE|ant|");
		
		// index package with dependency that is not indexed
		failure.add("INDEX|go|scala");
		// query package that is not indexed
		failure.add("QUERY|scala|");
		// remove package that is a dependency for other packages
		failure.add("REMOVE|java|");
		
		error.add("command package"); // no parameter delimiters
		error.add("|package"); // starting with parameter delimiters i.e, command missing/empty
		error.add("QUERY|package"); // 1 delimiter
		error.add("INDEX||"); // package is empty
		error.add("INVAL|ant|"); // not a valid command
		error.add(""); // empty input
		
	}
	
	@Test
	public void packageIndex_Success() {
		
		for (String input : success) {
			Assert.assertEquals(sendIndexingRequest(input), Response.OK.toString(),"Executing for "+input);
		}
	}
	
	@Test (dependsOnMethods = {"packageIndex_Success"})
	public void packageIndex_Failure() {
		for (String input : failure) {
			Assert.assertEquals(sendIndexingRequest(input), Response.FAIL.toString(),"Executing for "+input);
		}
	}
	
	@Test
	public void packageIndex_Error() {
		for (String input : error) {
			Assert.assertEquals(sendIndexingRequest(input), Response.ERROR.toString(),"Executing for "+input);
		}
		
	}
	
	@AfterClass
	public void stopClient() {
		// closing resources
		try {
			indexInput.close();
		} catch(Exception e) {
			
		}
		
		try {
			output.close();
		} catch(Exception e) {
			
		}
		
		try {
			clientSocket.close();
		} catch(Exception e) {
			
		}
	}
	
	private String sendIndexingRequest(String input) {
		try {
			indexInput.writeBytes(input + InputHandler.LINE_DELIMITER);
			return output.readLine();
		} catch (IOException e) {
			return "IO Error";
		}
		
	}
	
}