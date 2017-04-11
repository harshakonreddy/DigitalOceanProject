package com.digitalocean.packageindex.data;

public enum Response {
	OK("OK"), FAIL("FAIL"), ERROR("ERROR");

	private final String response;

	/**
	 * Private Constructor to provide constant values
	 * 
	 * @param text
	 */
	private Response(final String text) {
		this.response = text;
	}

	@Override
	public String toString() {
		return this.response;
	}
}
