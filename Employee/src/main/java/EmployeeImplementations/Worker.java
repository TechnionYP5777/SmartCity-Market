package EmployeeImplementations;

import com.google.inject.Inject;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeCommon.AEmployee;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * Worker - This class represent the worker functionality implementation.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17
 */

public class Worker extends AEmployee implements IWorker {


	@Inject
	public Worker(IClientRequestHandler clientRequestHandler) {

		this.clientRequestHandler = clientRequestHandler;

	}

	@Override
	public void login(String username, String password) {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);

		log.info("Creating login command wrapper with username: " + username + " and password: " + password);
		String serverResponse = sendRequestWithRespondToServer((new CommandWrapper(WorkerDefs.loginCommandSenderId,
				CommandDescriptor.LOGIN, Serialization.serialize(new Login(username, password))).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		clientId = commandDescriptor.getSenderID();
		this.username = username;
		this.password = password;
		log.info("Login to server succeed. Client id is: " + clientId);
		terminateCommunication();
	}

	@Override
	public void logout() {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		log.info("Creating logout command wrapper with username: " + username);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.LOGOUT, Serialization.serialize(username)))
						.serialize());
		resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		log.info("logout from server succeed.");
		log.info("Terminating the communiction with server");
		terminateCommunication();
	}

	@Override
	public CatalogProduct viewProductFromCatalog(int barcode) {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		log.info("Creating viewProductFromCatalog command wrapper with barcode: " + barcode);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(new SmartCode(barcode, null))).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		log.info("viewProductFromCatalog command succeed.");
		terminateCommunication();
		return Serialization.deserialize(commandDescriptor.getData(), CatalogProduct.class);
	}

	@Override
	public Login getWorkerLoginDetails() {
		return new Login(username, password);
	}

}
