package EmployeeImplementations;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeCommon.AEmployee;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;

/**
 * Worker - This class represent the worker functionality implementation.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17
 */

public class Worker extends AEmployee implements IWorker {

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
		String commandData = new Gson().toJson(login);
		CommandWrapper commandWrapper = new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGIN,
				commandData);
		String jsonResponse = null;
		LOGGER.log(Level.FINE, "Sending login command to server");
		try {
			jsonResponse = this.clientRequestHandler.sendRequestWithRespond(commandWrapper.toGson());			
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE,
					"Sending login command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new RuntimeException(e.getMessage());
		}
		CommandWrapper commandDescriptor = CommandWrapper.fromGson(jsonResponse);
		
		// TODO Talk about the way handling with the resultDescriptor
		
		clientId = commandDescriptor.getSenderID();
		this.username = username;
		this.password = password;
		LOGGER.log(Level.FINE, "Login to server succeed. Client id is: " + clientId);
	}

	@Override
	public void logout() {

		LOGGER.log(Level.FINE, "Creating logout command wrapper with username: " + username);
		String commandData = new Gson().toJson(username);
		CommandWrapper commandWrapper = new CommandWrapper(clientId, CommandDescriptor.LOGOUT, commandData);
		String jsonResponse = null;
		LOGGER.log(Level.FINE, "Sending logout command to server with clientId: " + clientId);
		try {
			jsonResponse = this.clientRequestHandler.sendRequestWithRespond(commandWrapper.toGson());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE,
					"Sending logout command to server encounter sever fault : " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		// TODO - delete this after talk about the way handling with the resultDescriptor
		if (jsonResponse != null)
			jsonResponse.length();

		LOGGER.log(Level.FINE, "logout from server succeed.");
		
		LOGGER.log(Level.FINE, "Terminate the communiction with server");
		terminateCommunication();
	}

	@Override
	public CatalogProduct viewProductFromCatalog(int barcode) {

		LOGGER.log(Level.FINE, "Creating viewProductFromCatalog command wrapper with barcode: " + barcode);
		String commandData = new Gson().toJson(barcode);
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
		// TODO Talk about the way handling with the resultDescriptor
		LOGGER.log(Level.FINE, "viewProductFromCatalog command succeed.");
		return new Gson().fromJson(commandDescriptor.getData(), CatalogProduct.class);
	}

}
