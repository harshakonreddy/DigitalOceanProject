package com.digitalocean.packageindex.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.digitalocean.packageindex.data.Command;
import com.digitalocean.packageindex.data.PackageInputBean;
import com.digitalocean.packageindex.data.Response;

/**
 * The Class InputHandlerTest unit tests class InputHandler.
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
		inputParseFailure.add("command package"); // no parameter delimiters
		inputParseFailure.add("|package"); // starting with parameter delimiters
											// i.e, command missing/empty
		inputParseFailure.add("command|package"); // 1 pipe
		inputParseFailure.add("command||"); // package is empty

		/* SUCCESS cases for executeCommand */

		// INDEX a new package with empty dependencies
		PackageInputBean successInputForIndex = getNewPackageInputBean(Command.INDEX.toString(), "A1");
		inputExecuteSuccess.add(successInputForIndex);
		successInputForIndex = getNewPackageInputBean(Command.INDEX.toString(), "A2");
		inputExecuteSuccess.add(successInputForIndex);

		// INDEX package with dependencies; dependencies were already indexed
		successInputForIndex = getNewPackageInputBean(Command.INDEX.toString(), "A", Arrays.asList("A1"));
		inputExecuteSuccess.add(successInputForIndex);
		successInputForIndex = getNewPackageInputBean(Command.INDEX.toString(), "B", Arrays.asList("A1"));
		inputExecuteSuccess.add(successInputForIndex);

		// INDEX existing package with new dependencies; dependencies were
		// already indexed
		successInputForIndex = getNewPackageInputBean(Command.INDEX.toString(), "A", Arrays.asList("A1", "A2"));
		inputExecuteSuccess.add(successInputForIndex);

		// QUERY for package with no dependencies
		PackageInputBean successInputForQuery = getNewPackageInputBean(Command.QUERY.toString(), "A1");
		inputExecuteSuccess.add(successInputForQuery);

		// QUERY for package with dependencies
		successInputForQuery = getNewPackageInputBean(Command.QUERY.toString(), "A");
		inputExecuteSuccess.add(successInputForQuery);

		// REMOVE package that is already indexed
		PackageInputBean successInputForRemove = getNewPackageInputBean(Command.REMOVE.toString(), "A");
		inputExecuteSuccess.add(successInputForRemove);

		// REMOVE package that is never indexed
		successInputForRemove = getNewPackageInputBean(Command.REMOVE.toString(), "C");
		inputExecuteSuccess.add(successInputForRemove);

		// REMOVE package that is a dependency
		successInputForRemove = getNewPackageInputBean(Command.REMOVE.toString(), "A1");
		inputExecuteFailure.add(successInputForRemove);
		
		/* FAILURE cases for executeCommand */
		// INDEX package with dependencies that are not indexed
		PackageInputBean failureInputForIndex = getNewPackageInputBean(Command.INDEX.toString(), "B",
				Arrays.asList("B1", "B2"));
		inputExecuteFailure.add(failureInputForIndex);

		// QUERY package; package is never indexed
		PackageInputBean failureInputForQuery = getNewPackageInputBean(Command.QUERY.toString(), "C");
		inputExecuteFailure.add(failureInputForQuery);

		/* ERROR cases for executeCommand */
		// Try to execute command other than INDEX,QUERY,REMOVE
		PackageInputBean errorInput = getNewPackageInputBean("INVALID", "ABC");
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
			Assert.assertEquals(
					inputHandler.executeCommand(input), (Response.OK.toString() + InputHandler.LINE_DELIMITER),
					"Executing for " + input.getCommand() + InputHandler.PARAM_DELIMITER + input.getPackageName());
		}
	}

	@Test(dependsOnMethods = {"executeCommand_Success"})
	public void executeCommand_Failure() {

		for (PackageInputBean input : inputExecuteFailure) {
			Assert.assertEquals(
					inputHandler.executeCommand(input), (Response.FAIL.toString() + InputHandler.LINE_DELIMITER),
					"Executing for " + input.getCommand() + InputHandler.PARAM_DELIMITER + input.getPackageName());
		}
	}

	@Test
	public void executeCommand_Error() {

		for (PackageInputBean input : inputExecuteError) {
			Assert.assertEquals(
					inputHandler.executeCommand(input), (Response.ERROR.toString() + InputHandler.LINE_DELIMITER),
					"Executing for " + input.getCommand() + InputHandler.PARAM_DELIMITER + input.getPackageName());
		}
	}

	private static PackageInputBean getNewPackageInputBean(String command, String aPackage) {

		return getNewPackageInputBean(command, aPackage, null);
	}

	private static PackageInputBean getNewPackageInputBean(String command, String aPackage, List<String> dependencies) {
		PackageInputBean newPackageInputBean = new PackageInputBean();
		newPackageInputBean.setCommand(command);
		newPackageInputBean.setPackageName(aPackage);
		newPackageInputBean.setDependencies(dependencies);

		return newPackageInputBean;
	}

}
