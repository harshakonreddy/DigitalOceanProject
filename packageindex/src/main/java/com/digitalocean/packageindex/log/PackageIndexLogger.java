package com.digitalocean.packageindex.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PackageIndexLogger {

	public static final Logger LOGGER = Logger.getLogger(PackageIndexLogger.class.getName());
	
	static {
		LOGGER.setLevel(Level.SEVERE);
	}
}
