package EmployeeCommon;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import ClientServerCommunication.ClientRequestHandler;

/** AEmployee - This abstract holds common functionality for 
 * the Employee such as worker, manager.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17 */

public abstract class AEmployee {

	protected ClientRequestHandler clientRequestHandler;
	protected static final Logger LOGGER = Logger.getLogger(ClientRequestHandler.class.getName());
	protected int clientId;
	protected String username;
	protected String password;
	

	protected void establishCommunication(int port, String host, int timeout)
			throws UnknownHostException, RuntimeException {
		clientRequestHandler = new ClientRequestHandler(port, host, timeout);
	}

	protected void terminateCommunication() {
		if (clientRequestHandler != null)
			clientRequestHandler.finishRequest();
	}
}
