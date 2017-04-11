package com.digitalocean.packageindex.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.digitalocean.packageindex.log.PackageIndexLogger;

/**
 * The Class PackageIndexingServer opens a server socket and accepts client
 * connections. It also spawns a thread for each incoming client connections so
 * that it can keep accepting new connections.
 */
public class PackageIndexingServer {

	static int clientCount = 0;
	private static final int DEFAULT_PORT = 8080;
	ServerSocket socket = null;
	private final Logger logger = PackageIndexLogger.LOGGER;

	/**
	 * Method to Start server, opens a TCP server socket on given port if port
	 * is free.
	 *
	 * @param port
	 *            is port on which to open server socket
	 */
	public void startServer(int port) {
		try {
			socket = new ServerSocket(port);
			logger.info("Server started... Accepting clients");
		} catch (IOException e) {
			logger.severe("Could not open socket at port " + port + ". Verify if port is free");
		}
	}

	/**
	 * Method to Stop server, simply closes the server socket.
	 */
	public void stopServer() {
		try {
			socket.close();
			logger.info("Server stopped!!!");
		} catch (IOException e) {
			logger.severe("Error when closing socket");
		}
	}

	/**
	 * Accepts incoming client connections and spawns a new thread for each
	 * client.
	 */
	public void acceptConnections() {

		try {
			while (true) {
				Socket clientConn = socket.accept();
				clientCount++;
				logger.info("Accepted client " + clientCount + " connection");
				IndexingServiceProvider indexService = new IndexingServiceProvider(clientConn);
				Thread clientThread = new Thread(indexService, "Thread_" + clientCount);
				clientThread.start();
			}
		} catch (IOException e) {
			logger.severe("Error accepting client connection...");
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		PackageIndexingServer pis = new PackageIndexingServer();
		if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
			pis.startServer(Integer.parseInt(args[0]));
		} else {
			pis.startServer(DEFAULT_PORT);
		}
		pis.acceptConnections();
	}
}
