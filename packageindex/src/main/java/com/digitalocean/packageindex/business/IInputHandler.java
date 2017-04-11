package com.digitalocean.packageindex.business;

import com.digitalocean.packageindex.data.PackageInputBean;

/**
 * The Interface IInputHandler provides methods to parse client input and
 * execute the command.
 */
public interface IInputHandler {

	/**
	 * Validates and parses the input into PackageInputBean.
	 *
	 * @param input
	 * @return the package input bean object if input is valid, null otherwise
	 */
	public PackageInputBean getPackageInputBean(String input);

	/**
	 * Executes command present in packageInputBean.
	 *
	 * @param packageInputBean
	 *            contains command to be executed, packageName and dependencies
	 * @return OK, FAIL or ERROR based on result of command execution.
	 */
	public String executeCommand(PackageInputBean packageInputBean);
}
