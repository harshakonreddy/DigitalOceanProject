package com.digitalocean.packageindex.business;

import java.util.Arrays;
import java.util.List;

import com.digitalocean.packageindex.data.Command;
import com.digitalocean.packageindex.data.PackageInputBean;
import com.digitalocean.packageindex.data.Response;

/**
 * The Class InputHandler is responsible for parsing the input and
 * executing the command present in input and return appropriate response.
 */
public class InputHandler implements IInputHandler{

	private IPackageIndexer packageIndexer = new PackageIndexer();
	
	// Delimiters
	public static final String LINE_DELIMITER = "\n";
	public static final String DEPENDENCY_DELIMITER = ",";
	public static final char PARAM_DELIMITER = '|';
	public static final String PARAM_DELIMITER_REGEX = "\\|";
	
	@Override
	public PackageInputBean getPackageInputBean(String input) {

		if(isInValidInput(input)) return null;
		
		String[] inputParameters = input.split(String.valueOf(PARAM_DELIMITER_REGEX));
		PackageInputBean packageInputBean = new PackageInputBean();
		packageInputBean.setCommand(inputParameters[0]);
		packageInputBean.setPackageName(inputParameters[1]);
		
		if(inputParameters.length > 2) {
			String dependencies = inputParameters[2];
			List<String> dependencyList = null;
			if(!dependencies.isEmpty()) {
				dependencyList = Arrays.asList(dependencies.split(DEPENDENCY_DELIMITER));
			} else {
				dependencyList = null;
			}
			packageInputBean.setDependencies(dependencyList);
		}
		
		return packageInputBean;
	}

	/**
	 * Method to validate if input provided is adhering to correct format.
	 * Valid input will have 2 PARAM_DELIMITERs, and of format <command>|<packageName>|[Dependencies]
	 * where command and package are mandatory and dependencies are optional
	 * 
	 * @param input
	 * @return true, if is in valid input
	 */
	private boolean isInValidInput(String input) {
		
		if(input == null || input.isEmpty())
			return true;
		
		int firstPipeIndex = input.indexOf(PARAM_DELIMITER);
		int secondPipeIndex = input.indexOf(PARAM_DELIMITER,firstPipeIndex + 1);
		
		/* if there are no PARAM_DELIMITERs or starts with a PARAM_DELIMITER
		 *  (Meaning command is missing) or only single PARAM_DELIMITER input is not valid */
		if(firstPipeIndex == -1 || firstPipeIndex == 0 || secondPipeIndex == -1 ) 
			return true;
		
		// Means package name is empty
		if(secondPipeIndex - firstPipeIndex < 2 ) 
			return true;
		
		return false;
	}

	@Override
	public String executeCommand(PackageInputBean packageInputBean) {
		String response = "";
		if(packageInputBean.getCommand().equalsIgnoreCase(Command.INDEX.toString())) {
			// think of case where dependency is same as package
			response = packageIndexer.indexPackage(packageInputBean.getPackageName(),packageInputBean.getDependencies());
		} else if(packageInputBean.getCommand().equalsIgnoreCase(Command.QUERY.toString())) {
			response = packageIndexer.queryPackage(packageInputBean.getPackageName());
		} else if(packageInputBean.getCommand().equalsIgnoreCase(Command.REMOVE.toString())) {
			response = packageIndexer.removePackageFromIndex(packageInputBean.getPackageName());//should removing package remove dependency?
		} else {
			response = Response.ERROR.toString();
		}
		
		return response + LINE_DELIMITER; 
	}
	
}
