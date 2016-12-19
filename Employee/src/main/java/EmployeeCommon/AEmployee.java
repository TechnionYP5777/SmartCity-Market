package EmployeeCommon;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ClientServerApi.ResultDescriptor;
import ClientServerCommunication.ClientRequestHandler;
import EmployeeDefs.WorkerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsContracts.ISerialization;

/**
 * AEmployee - This abstract holds common functionality for the Employee such as
 * worker, manager.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17
 */

public abstract class AEmployee {

	protected ISerialization serialization;
	protected IClientRequestHandler clientRequestHandler;
	protected static final Logger LOGGER = Logger.getLogger(ClientRequestHandler.class.getName());
	protected int clientId;
	protected String username;
	protected String password;

	protected void establishCommunication(int port, String host, int timeout) {
		LOGGER.log(Level.FINE,
				"Establish communication with server: port: " + WorkerDefs.port + " host: " + WorkerDefs.host);
		try {
			clientRequestHandler.createSocket(port, host, timeout);
		} catch (UnknownHostException | RuntimeException e) {
			LOGGER.log(Level.SEVERE,
					"Creating communication with the server encounter severe fault: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void terminateCommunication() {
		if (clientRequestHandler != null)
			clientRequestHandler.finishRequest();
	}

	protected String sendRequestWithRespondToServer(String request) {
		LOGGER.log(Level.FINE, "Sending command to server");
		try {
			return this.clientRequestHandler.sendRequestWithRespond(request);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void resultDescriptorHandler(ResultDescriptor ¢) {

		switch (¢) {

		case SM_OK:
			break;
		case SM_INVALID_SENDER_ID:
			break;
		case SM_CATALOG_PRODUCT_DOES_NOT_EXIST:
			throw new RuntimeException("Catalog product could not be found");
		case SM_ERR:
			break;
		case SM_INVALID_CMD_DESCRIPTOR:
			break;
		case SM_INVALID_PARAMETER:
			break;
		case SM_SENDER_IS_ALREADY_CONNECTED:
			throw new RuntimeException("The user is already connected to the server");
		case SM_SENDER_IS_NOT_CONNECTED:
			throw new RuntimeException("The user is not connected to the server");
		case SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD:
			throw new RuntimeException("Wrong username or password");
		default:
			break;
		}
	}

}