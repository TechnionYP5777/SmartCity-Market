package ClientServerCommunication;

import java.net.Socket;

/** ProcessRequest - abstract class to be overridden.
 * The process request will send/receive information on a socket and 
 * will process the request.
 * @author Aviad Cohen
 * @since 2016-12-08 */

public interface ProcessRequest {
	void process(Socket s); 
}