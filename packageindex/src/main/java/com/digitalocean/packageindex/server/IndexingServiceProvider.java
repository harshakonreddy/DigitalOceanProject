package com.digitalocean.packageindex.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

import com.digitalocean.packageindex.business.IInputHandler;
import com.digitalocean.packageindex.business.InputHandler;
import com.digitalocean.packageindex.data.PackageInputBean;
import com.digitalocean.packageindex.data.Response;
import com.digitalocean.packageindex.log.PackageIndexLogger;

/**
 * The Class IndexingServiceProvider is a thread to serve each client that sends
 * indexing requests.
 */
public class IndexingServiceProvider implements Runnable {

	private Socket clientConn = null;
	private IInputHandler inputHandler = new InputHandler();
	private final Logger logger = PackageIndexLogger.LOGGER;

	/**
	 * @param pClientConn
	 *            is the client socket to read and write to
	 */
	IndexingServiceProvider(Socket pClientConn) {
		this.clientConn = pClientConn;
	}

	/**
	 * @param pClientConn
	 *            is the client socket to read and write to.
	 */
	@Override
	public void run() {
		try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));
				DataOutputStream outputWriter = new DataOutputStream(clientConn.getOutputStream());) {

			/*
			 * Read input from socket and delegate to InputHandler and Write the
			 * response back to client socket
			 */
			while (true) {
				String input = inputReader.readLine();
				logger.info("Input from Client " + Thread.currentThread().getName() + " = " + input);
				try {
					PackageInputBean packageInputBean = inputHandler.getPackageInputBean(input);

					if (null != packageInputBean) {
						String response = inputHandler.executeCommand(packageInputBean);
						logger.info("Response for input " + input + " = " + response);
						outputWriter.writeBytes(response);
					} else {
						logger.info("Invalid Input::" + input);
						outputWriter.writeBytes(Response.ERROR.toString() + InputHandler.LINE_DELIMITER);
					}
				} catch (Exception e) {
					logger.info("Something went wrong processing client request. Exception message for Input '" + input
							+ " is " + e.getMessage());
					logger.severe("Trace for Input '" + input + " is " + Arrays.toString(e.getStackTrace()));
					outputWriter.writeBytes(Response.ERROR.toString() + InputHandler.LINE_DELIMITER);
				}
				outputWriter.flush();
			}

		} catch (IOException e) {
			logger.info("Client ended connection/timedout for " + Thread.currentThread().getName());
			logger.severe("Trace for read/write issue :: " + Arrays.toString(e.getStackTrace()));
		} finally {
			try {
				clientConn.close();
				logger.info("Closing client socket for " + Thread.currentThread().getName());
			} catch (IOException e) {
				logger.info("Could not close client socket");
				logger.severe("Trace for closing client socket:: " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

}
