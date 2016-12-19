package EmployeeImplementations;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeCommon.AEmployee;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsContracts.ISerialization;

/**
 * Worker - This class represent the worker functionality implementation.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17
 */

public class Worker extends AEmployee implements IWorker {
	
	
	public Worker(ISerialization serialization, IClientRequestHandler clientRequestHandler) {
		
		this.serialization = serialization;
		this.clientRequestHandler = clientRequestHandler;
		
	}

	@Override
	public void login(String username, String password) {
		LOGGER.log(Level.FINE,
				"Establish communication with server: port: " + WorkerDefs.port + " host: " + WorkerDefs.host);
		try {
			establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		} catch (UnknownHostException | RuntimeException e) {
			LOGGER.log(Level.SEVERE,
					"Creating communication with the server encounter severe fault: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		LOGGER.log(Level.FINE,
				"Creating login command wrapper with username: " + username + " and password: " + password);
		Login login = new Login(username, password);
		String commandData = serialization.serialize(login);
		CommandWrapper commandWrapper = new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGIN,
				commandData);
		String jsonResponse = null;
		LOGGER.log(Level.FINE, "Sending login command to server");
		try {
			jsonResponse = this.clientRequestHandler.sendRequestWithRespond(commandWrapper.toGson());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Sending login command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new RuntimeException(e.getMessage());
		}
		CommandWrapper commandDescriptor = CommandWrapper.fromGson(jsonResponse);

		resultDescriptorHandler(commandDescriptor.getResultDescriptor());

		clientId = commandDescriptor.getSenderID();
		this.username = username;
		this.password = password;
		LOGGER.log(Level.FINE, "Login to server succeed. Client id is: " + clientId);
	}

	@Override
	public void logout() {

		LOGGER.log(Level.FINE, "Creating logout command wrapper with username: " + username);
		String commandData = serialization.serialize(username);
		CommandWrapper commandWrapper = new CommandWrapper(clientId, CommandDescriptor.LOGOUT, commandData);
		String jsonResponse = null;
		LOGGER.log(Level.FINE, "Sending logout command to server with clientId: " + clientId);
		try {
			jsonResponse = this.clientRequestHandler.sendRequestWithRespond(commandWrapper.toGson());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Sending logout command to server encounter sever fault : " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		resultDescriptorHandler(CommandWrapper.fromGson(jsonResponse).getResultDescriptor());
			
		LOGGER.log(Level.FINE, "logout from server succeed.");

		LOGGER.log(Level.FINE, "Terminating the communiction with server");
		terminateCommunication();
	}

	@Override
	public CatalogProduct viewProductFromCatalog(int barcode) {

		LOGGER.log(Level.FINE, "Creating viewProductFromCatalog command wrapper with barcode: " + barcode);
		String commandData = serialization.serialize(barcode);
		CommandWrapper commandWrapper = new CommandWrapper(clientId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
				commandData);
		String jsonResponse = null;
		LOGGER.log(Level.FINE, "Sending viewProductFromCatalog command to server with clientId: " + clientId);
		try {
			jsonResponse = this.clientRequestHandler.sendRequestWithRespond(commandWrapper.toGson());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE,
					"Sending viewProductFromCatalog command to server encounter sever fault : " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		CommandWrapper commandDescriptor = CommandWrapper.fromGson(jsonResponse);
		
		resultDescriptorHandler(commandDescriptor.getResultDescriptor());

		LOGGER.log(Level.FINE, "viewProductFromCatalog command succeed.");
		return serialization.deserialize(commandDescriptor.getData(), CatalogProduct.class);
	}

}
