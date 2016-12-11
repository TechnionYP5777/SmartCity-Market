package main.ClientServerCommunication;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/** ThreadPooledServer - Handles the server connection between and multiple clients.
 * The name says it all - the server will have a pool for all the clients requests
 * and an idle thread will process the thread using ServerWorkerRunnable.
 * @author Aviad Cohen
 * @since 2016-12-10*/

public class ThreadPooledServer implements Runnable{

	private static final Logger LOGGER = Logger.getLogger(ThreadPooledServer.class.getName());
	
	private int serverPort;
	private ServerSocket serverSocket;
	private boolean isStopped;
	private ExecutorService threadPool;
	private ProcessRequest processRequest;

	// TODO Currently, the server supports only "localhost" (127.0.0.1). Need to add support to connection by selected ip.
	
	/**
	 * Create the thread pool server.
	 * @param serverPort - the server port.
	 * @param numOfThreads - the number of threads which will work concurrently.
	 * @param processRequest - the object which processes the clients requests (also send
	 * responds and receive requests.
	 */
    public ThreadPooledServer(int serverPort, int numOfThreads, ProcessRequest processRequest) {
        this.serverPort = serverPort;
        this.serverSocket = null;
        this.isStopped = false;
        this.processRequest = processRequest;
        
        if (processRequest == null)
			throw new NullPointerException("Server failed in initialization, processRequest can't be null");
        
        if (numOfThreads < 0)
			throw new RuntimeException(
					"Server failed in initialization, number of threads " + numOfThreads + " must be positve");
        
        threadPool = Executors.newFixedThreadPool(numOfThreads);
        
        LOGGER.log(Level.FINE, "Server initialized successfully with " + numOfThreads + " threads, port number " + serverPort);
    }

    /**
     * The main function which will always run when the server is working.
     */
    public void run(){
        synchronized(this){
            LOGGER.log(Level.FINE, "Server starts running");
        }
        
        openServerSocket();
        
        while(!isStopped){
            Socket clientSocket;

            try {
            	/* Accepts new client request */
                clientSocket = this.serverSocket.accept();
                
                LOGGER.log(Level.FINE, "Server accepted client request on port " + clientSocket.getPort());
            } catch (IOException e) {
                if (!isStopped)
					throw new RuntimeException("Server failed to accept client connection", e);
                
                LOGGER.log(Level.FINE, "Server got interrupt, stop receiving client requests");
				break;
            }
             
            /* Insert the client request to the thread pool and executes it when 
             * a thread will be available */
            this.threadPool.execute(
                new ServerWorkerRunnable(clientSocket, processRequest));
            
            LOGGER.log(Level.FINE, "Server added client request on port " + clientSocket.getPort() + " to execution pool");
        }
        
        this.threadPool.shutdown();
        
        LOGGER.log(Level.FINE, "Server shuted down successfully");
    }

    /**
     * stop - stop the server from getting new request and close the server socket.
     */
    public synchronized void stop(){
        this.isStopped = true;
        
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Server failed to close socket", e);
        }
        
        LOGGER.log(Level.FINE, "Server closed socket succesfully");
    }

    /**
     * isStopped
     * @return - true if the server stopped, otherwise false.
     */
    public synchronized boolean isStopped(){
    	String state = isStopped ? "running" : "no running";
        LOGGER.log(Level.FINE, "Server is currently " + state);
        
        return isStopped;
    }
    
    /**
     * openServerSocket - opens a server socket to receive new client requests.
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Server failed to open port " + serverPort, e);
        }
        
        LOGGER.log(Level.FINE, "Server opened socket on port " + serverPort + " successfully");
    }
}
