package UtilsContracts;

import java.io.IOException;
import java.net.UnknownHostException;

/** IClientRequestHandler - This interface represents the functionality of the client request from the server.
 * @author Shimon Azulay
 * @since 2016-12-19 */

public interface IClientRequestHandler {
	
    /** 
     * Create socket for a given server.
     * @param serverPort - the server port number.
     * 		  serverHostName - the server host name.
     * 		  timeout - the timeout for send/receive messages in milliseconds.
     * @throws UnknownHostException, RuntimeException
     */
	void createSocket(int serverPort, String serverHostName, int timeout) throws UnknownHostException, RuntimeException;

    /** 
     * Send a request to the server and returns the respond.
     * @param request - the request to be sent.
     * @return - the server respond.
     * This function might throw exception due to timeout/failure.
     * This is the safest way to send a request.
     */
	String sendRequestWithRespond(String request) throws IOException;
	
    /** 
     * Send a request to the server without getting back respond.
     * @param request - the request to be sent.
     * Use this function only if you know what you are doing - not getting back a 
     * respond can be unsafe!
     */
	void sendRequestNoRespond(String request) throws IOException;
	
    /** 
     * Receive a respond from the server.
     * @return - the server respond.
     * This function might throw exception due to timeout/failure.
     */
	String waitForRespond() throws IOException;

	/**
	 * Close socket with the server.
	 */
	void finishRequest();
	
	
}
