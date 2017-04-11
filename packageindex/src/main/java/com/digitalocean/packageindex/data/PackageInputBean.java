package com.digitalocean.packageindex.data;

import java.util.List;

/**
 * This bean class encapsulates the parsed input for index request
 */
public class PackageInputBean {
	private String packageName = null;
	private String command = null;
	private List<String> dependencies = null;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

}
