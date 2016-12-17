package ClientCommon;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import ClientServerCommunication.ClientRequestHandler;

/** ClientCommunicationHandler - This abstract holds common functionality for 
 * the clients such as worker, manager, cart.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17 */

public abstract class ClientCommunicationHandler {

	protected ClientRequestHandler clientRequestHandler;
	protected static final Logger LOGGER = Logger.getLogger(ClientRequestHandler.class.getName());

	protected void establishCommunication(int port, String host, int timeout)
			throws UnknownHostException, RuntimeException {
		clientRequestHandler = new ClientRequestHandler(port, host, timeout);
	}

	protected void terminateCommunication() {
		if (clientRequestHandler != null) {
			clientRequestHandler.finishRequest();
		}
	}
}
