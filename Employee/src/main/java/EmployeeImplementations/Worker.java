package EmployeeImplementations;

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
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);

		LOGGER.log(Level.FINE,
				"Creating login command wrapper with username: " + username + " and password: " + password);
		Login login = new Login(username, password);
		String commandData = serialization.serialize(login);
		CommandWrapper commandWrapper = new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGIN,
				commandData);
		String jsonResponse = sendRequestWithRespondToServer(commandWrapper.toGson());
		CommandWrapper commandDescriptor = CommandWrapper.fromGson(jsonResponse);
		resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		clientId = commandDescriptor.getSenderID();
		this.username = username;
		this.password = password;
		LOGGER.log(Level.FINE, "Login to server succeed. Client id is: " + clientId);
		terminateCommunication();
	}

	@Override
	public void logout() {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		LOGGER.log(Level.FINE, "Creating logout command wrapper with username: " + username);
		String commandData = serialization.serialize(username);
		CommandWrapper commandWrapper = new CommandWrapper(clientId, CommandDescriptor.LOGOUT, commandData);
		String jsonResponse = sendRequestWithRespondToServer(commandWrapper.toGson());
		resultDescriptorHandler(CommandWrapper.fromGson(jsonResponse).getResultDescriptor());
		LOGGER.log(Level.FINE, "logout from server succeed.");
		LOGGER.log(Level.FINE, "Terminating the communiction with server");
		terminateCommunication();
	}

	@Override
	public CatalogProduct viewProductFromCatalog(int barcode) {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		LOGGER.log(Level.FINE, "Creating viewProductFromCatalog command wrapper with barcode: " + barcode);
		String commandData = serialization.serialize(barcode);
		CommandWrapper commandWrapper = new CommandWrapper(clientId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
				commandData);
		String jsonResponse = sendRequestWithRespondToServer(commandWrapper.toGson());
		CommandWrapper commandDescriptor = CommandWrapper.fromGson(jsonResponse);
		resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		LOGGER.log(Level.FINE, "viewProductFromCatalog command succeed.");
		terminateCommunication();
		return serialization.deserialize(commandDescriptor.getData(), CatalogProduct.class);
	}

}
