package ClientServerCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** ClientRequestHandler - Handles the client request from the server.
 * The handler opens a socket for the client an handles the client requests
 * sending and receiving the responds.
 * @author Aviad Cohen
 * @since 2016-12-08 */

public class ClientRequestHandler {
	
	private static final Logger LOGGER = Logger.getLogger(ClientRequestHandler.class.getName());
	
    private Socket socket;
    
    /** 
     * Create a client request handler and opens a client socket for a given server.
     * @param serverPort - the server port number.
     * 		  serverHostName - the server host name.
     * 		  timeout - the timeout for send/receive messages in milliseconds.
     * @throws UnknownHostException, RuntimeException
     */
    public ClientRequestHandler(int serverPort, String serverHostName, int timeout) throws UnknownHostException, RuntimeException {    	
  	   	if (timeout <= 0)
			throw new RuntimeException("Client request manager failed to initialize with non positive timeout "
					+ timeout + " miliseconds");
    	
    	try {
    		socket = new Socket();
    		socket.connect(new InetSocketAddress(serverHostName, serverPort), timeout);
    	} catch (UnknownHostException e) {    		
			throw new UnknownHostException(
					"Client request handler failed to find host " + serverHostName + " on port " + serverPort);
        } catch (IOException e) {
			throw new RuntimeException(
					"Client request handler failed to get I/O for the connection to " + serverHostName + " on port " + serverPort, e);
        }
    	
    	try {
			socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			throw new RuntimeException(
					"Client request manager failed to set timeout " + timeout + " miliseconds", e);
		}
    	
    	LOGGER.log(Level.FINE, "Client request handler initialized successfully with host " + serverHostName + " on port " + serverPort);
    }

    /** 
     * Receive a respond from the server.
     * @return - the server respond.
     */
    private String getRespond() throws IOException {
        try {
        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	String $ = null;
        	
    		while ($ == null)
    			$ = in.readLine();
    		
        	LOGGER.log(Level.FINE, "Client request handler got respond: " + $);
        	
        	return $;
		} catch (java.net.SocketTimeoutException e) {
			throw new java.net.SocketTimeoutException("Client request manager failed to get respond due to timeout");
		} catch (IOException e) {
			throw new RuntimeException("Client request manager failed to get respond", e);
		}
    }
    
    /** 
     * Send a request to the server.
     * @param request - the request to be sent.
     */
    private void sendRequest(String request) throws IOException {
		try {
	    	(new PrintWriter(socket.getOutputStream(), true)).println(request);
	    	
	    	LOGGER.log(Level.FINE, "Client request handler sent request: " + request);
		} catch (IOException e) {
			throw new RuntimeException("Client request handler failed to send request: " + request, e);
		}
    }
    
    /** 
     * Send a request to the server and returns the respond.
     * @param request - the request to be sent.
     * @return - the server respond.
     * This function might throw exception due to timeout/failure.
     * This is the safest way to send a request.
     */
	public String sendRequestWithRespond(String request) throws IOException {
		sendRequest(request);
		
		return getRespond();
	}
	
    /** 
     * Send a request to the server without getting back respond.
     * @param request - the request to be sent.
     * Use this function only if you know what you are doing - not getting back a 
     * respond can be unsafe!
     */
	public void sendRequestNoRespond(String request) throws IOException {
		sendRequest(request);
	}
	
    /** 
     * Receive a respond from the server.
     * @return - the server respond.
     * This function might throw exception due to timeout/failure.
     */
	public String waitForRespond() throws IOException {
		return getRespond();
	}

	/**
	 * Close socket with the server.
	 */
	public void finishRequest() {
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException("Client request handler failed to close socket", e);
		}
	}
}
