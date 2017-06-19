package ClientServerCommunication;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

import ClientServerCommunication.ProcessRequest;
import ClientServerCommunication.ServerWorkerRunnable;

/** ThreadPooledServer - Handles the server connection between and multiple clients.
 * The name says it all - the server will have a pool for all the clients requests
 * and an idle thread will process the thread using ServerWorkerRunnable.
 * @author Aviad Cohen
 * @since 2016-12-10*/

public class ThreadPooledServer implements Runnable{

	private static final int backlog = 50;
	static Logger log = Logger.getLogger(ThreadPooledServer.class.getName());
	
	private int serverPort;
	private ServerSocket serverSocket;
	private InetAddress ipAddress;
	private boolean isStopped;
	private ExecutorService threadPool;
	private ProcessRequest processRequest;
	
	/**
	 * Create the thread pool server.
	 * @param serverPort - the server port.
	 * @param numOfThreads - the number of threads which will work concurrently.
	 * @param r - the object which processes the clients requests (also send
	 * responds and receive requests.
	 * @param ipAddress - the server IP address to listen on
	 * @throws UnknownHostException 
	 */
	private void initializeServer(int serverPort, int numOfThreads, ProcessRequest r, String ipAddress) throws UnknownHostException {
        this.serverPort = serverPort;
        this.isStopped = false;
        this.processRequest = r;
        
        try {
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			throw new UnknownHostException("Server failed in initialization, unknown ip address " + ipAddress);
		}
        
        if (r == null)
			throw new NullPointerException("Server failed in initialization, processRequest can't be null");
        
        if (numOfThreads < 0)
			throw new RuntimeException(
					"Server failed in initialization, number of threads " + numOfThreads + " must be positve");
        
        threadPool = Executors.newFixedThreadPool(numOfThreads);
        
        log.info("Server initialized successfully with " + numOfThreads + " threads, port number " + serverPort);
	}

    public ThreadPooledServer(int serverPort, int numOfThreads, ProcessRequest processRequest) throws UnknownHostException {
    	/* Use default IP is localhost */
    	initializeServer(serverPort, numOfThreads, processRequest, "127.0.0.1");
    }
	
    public ThreadPooledServer(int serverPort, int numOfThreads, ProcessRequest processRequest, String ipAddress) throws UnknownHostException {
    	initializeServer(serverPort, numOfThreads, processRequest, ipAddress);
    }

    /**
     * The main function which will always run when the server is working.
     */
    @Override
	public void run(){
        synchronized(this){
        	log.info("Server starts running");
        }
        
        openServerSocket();
        
        while(!isStopped){
            Socket clientSocket;

            try {
            	/* Accepts new client request */
                clientSocket = this.serverSocket.accept();
                
                log.info("Server accepted client request on port " + clientSocket.getPort());
            } catch (IOException e) {
                if (!isStopped)
					throw new RuntimeException("Server failed to accept client connection", e);
                
                log.info("Server got interrupt, stop receiving client requests");
				break;
            }
             
            /* Insert the client request to the thread pool and executes it when 
             * a thread will be available */
            this.threadPool.execute(
                new ServerWorkerRunnable(clientSocket, processRequest));
            
            log.info("Server added client request on port " + clientSocket.getPort() + " to execution pool");
        }
        
        this.threadPool.shutdown();
        
        log.info("Server shuted down successfully");
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
        
        log.info("Server closed socket succesfully");
    }

    /**
     * isStopped
     * @return - true if the server stopped, otherwise false.
     */
    public synchronized boolean isStopped(){
    	String state = (isStopped ? "" : "no ") + "running";
    	log.info("Server is currently " + state);
        
        return isStopped;
    }
    
    /**
     * openServerSocket - opens a server socket to receive new client requests.
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort, backlog, this.ipAddress);
        } catch (IOException e) {
            throw new RuntimeException("Server failed to open port " + serverPort, e);
        }
        
        log.info("Server opened socket on port " + serverPort + " successfully");
    }
}
