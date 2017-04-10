package com.digitalocean.packageindex.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.digitalocean.packageindex.business.IInputHandler;
import com.digitalocean.packageindex.business.InputHandler;
import com.digitalocean.packageindex.data.PackageInputBean;

/**
 * The Class IndexingServiceProvider is a thread to serve each client that 
 * sends indexing requests.
 */
public class IndexingServiceProvider implements Runnable {

	private Socket clientConn = null;
	private IInputHandler inputHandler = new InputHandler();
	
	/**
	 * @param pClientConn is the client socket to read and write to
	 */
	IndexingServiceProvider(Socket pClientConn) {
		this.clientConn = pClientConn;
	}
	
	/**
	 * @param pClientConn is the client socket to read and write to.
	 */
	@Override
	public void run() {
		try(
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));
				DataOutputStream outputWriter = new DataOutputStream(clientConn.getOutputStream());) {
			
			/* Read input from socket and delegate to InputHandler and
			 * Write the response back to client socket
			 */
			while(true) {
				String input = inputReader.readLine();
				
				try {
					PackageInputBean packageInputBean = inputHandler.getPackageInputBean(input);
					
					if(null != packageInputBean) {
						String response = inputHandler.executeCommand(packageInputBean);
						outputWriter.writeBytes(response);
					} else {
						outputWriter.writeBytes("ERROR\n");
					}
				} catch(Exception e) {
					outputWriter.writeBytes("ERROR\n");
				}
				outputWriter.flush();
			}
			
		} catch (IOException e) {
			
		} finally {
			try {
				clientConn.close();
			} catch (IOException e) {
				
			}
		}
	}

}
