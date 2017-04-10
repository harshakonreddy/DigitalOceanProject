package com.digitalocean.packageindex.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.digitalocean.packageindex.data.Command;
import com.digitalocean.packageindex.data.PackageInputBean;
import com.digitalocean.packageindex.data.Response;

/**
 * The Class InputHandler.
 */
public class InputHandlerTest {

	private static IInputHandler inputHandler = new InputHandler();
	private static List<String> inputParseSuccess = null;
	private static List<String> inputParseFailure = null; 
	private static List<PackageInputBean> inputExecuteSuccess = null;
	private static List<PackageInputBean> inputExecuteFailure = null;
    private static List<PackageInputBean> inputExecuteError = null;
    
    @BeforeClass
	public static void setUp() {
		inputParseSuccess = new ArrayList<String>();
		inputParseFailure = new ArrayList<String>();
		inputExecuteSuccess = new ArrayList<PackageInputBean>();
		inputExecuteFailure = new ArrayList<PackageInputBean>();
		inputExecuteError = new ArrayList<PackageInputBean>();
        
        inputParseSuccess.add("command|package|\n");
        inputParseSuccess.add("command|package|package dependencies\n");
		inputParseSuccess.add("command|package|package dependency1, package dependency2 \n");
        
		inputParseFailure.add(null);
        inputParseFailure.add("");
        inputParseFailure.add("command package"); //no parameter delimiters
        inputParseFailure.add("|package"); //starting with parameter delimiters i.e, command missing/empty
        inputParseFailure.add("command|package"); //1 pipe
        inputParseFailure.add("command||"); // package is empty
		
        PackageInputBean successInputForIndex = getNewPackageInutBean(Command.INDEX.toString(),"A1",null);
        inputExecuteSuccess.add(successInputForIndex);
        
        successInputForIndex = getNewPackageInutBean(Command.INDEX.toString(),"A2",null);
        inputExecuteSuccess.add(successInputForIndex);
        
        successInputForIndex = getNewPackageInutBean(Command.INDEX.toString(),"A",Arrays.asList("A1"));
        inputExecuteSuccess.add(successInputForIndex);
        
        //update existing package with new dependencies
        successInputForIndex = getNewPackageInutBean(Command.INDEX.toString(),"A",Arrays.asList("A1","A2"));
        inputExecuteSuccess.add(successInputForIndex);
        
        successInputForIndex = getNewPackageInutBean(Command.INDEX.toString(),"B",Arrays.asList("A1"));
        inputExecuteSuccess.add(successInputForIndex);
                
        PackageInputBean successInputForQuery = getNewPackageInutBean(Command.QUERY.toString(),"A1",null);
        inputExecuteSuccess.add(successInputForQuery);
        
        successInputForQuery = getNewPackageInutBean(Command.QUERY.toString(),"A",null);
        inputExecuteSuccess.add(successInputForQuery);
        
        PackageInputBean successInputForRemove = getNewPackageInutBean(Command.REMOVE.toString(),"A",null);
        inputExecuteSuccess.add(successInputForRemove);
        
        PackageInputBean errorInput = getNewPackageInutBean("INVALID","ABC",null);
		inputExecuteError.add(errorInput);
	}
	
	@Test
	public void getPackageInputBean_Success() {
		
		for (String input : inputParseSuccess) {
			Assert.assertNotNull(inputHandler.getPackageInputBean(input));
		}
	}
	
	@Test
	public void getPackageInputBean_Failure() {
		
		for (String input : inputParseFailure) {
			Assert.assertNull(inputHandler.getPackageInputBean(input));
		}
	}
    
    @Test
    public void executeCommand_Success() {
        
        for (PackageInputBean input : inputExecuteSuccess) {
            Assert.assertEquals(inputHandler.executeCommand(input),Response.OK.toString()+InputHandler.LINE_DELIMITER);
        }
    }
    
    @Test
    public void executeCommand_Failure() {
        
        for (PackageInputBean input : inputExecuteFailure) {
            Assert.assertEquals(inputHandler.executeCommand(input),Response.FAIL.toString()+InputHandler.LINE_DELIMITER);
        }
    }
    
    @Test
    public void executeCommand_Error() {
        
        for (PackageInputBean input : inputExecuteError) {
            Assert.assertEquals(inputHandler.executeCommand(input),Response.ERROR.toString()+InputHandler.LINE_DELIMITER);
        }
    }
    
    private static PackageInputBean getNewPackageInutBean(String command, String aPackage, List<String> dependencies) {
    	PackageInputBean newPackageInputBean = new PackageInputBean();
    	newPackageInputBean.setCommand(command);
    	newPackageInputBean.setPackageName(aPackage);
    	newPackageInputBean.setDependencies(dependencies);
    	
    	return newPackageInputBean;
    }
	
}
