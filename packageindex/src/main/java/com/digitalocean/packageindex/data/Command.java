package com.digitalocean.packageindex.data;

public enum Command {
	INDEX("INDEX"), QUERY("QUERY"), REMOVE("REMOVE");

	private final String command;

	/**
	 * Private Constructor to provide constant values
	 * 
	 * @param text
	 */
	private Command(final String text) {
		this.command = text;
	}

	@Override
	public String toString() {
		return this.command;
	}
}
