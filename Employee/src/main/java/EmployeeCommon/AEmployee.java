package EmployeeCommon;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import ClientServerApi.ResultDescriptor;
import ClientServerCommunication.ClientRequestHandler;
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

	protected void establishCommunication(int port, String host, int timeout)
			throws UnknownHostException, RuntimeException {
		clientRequestHandler.createSocket(port, host, timeout);
	}

	protected void terminateCommunication() {
		if (clientRequestHandler != null)
			clientRequestHandler.finishRequest();
	}

	// TODO Add protected handler to each of them.
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
