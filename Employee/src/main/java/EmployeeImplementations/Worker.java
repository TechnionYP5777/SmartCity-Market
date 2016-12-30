package EmployeeImplementations;

import com.google.inject.Inject;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import CommonDefs.CLIENT_TYPE;
import EmployeeCommon.AEmployee;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.UnknownSenderID;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * Worker - This class represent the worker functionality implementation.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen
 * @since 2016-12-17
 */

public class Worker extends AEmployee implements IWorker {
	
	@Inject
	public Worker(IClientRequestHandler clientRequestHandler) {

		this.clientRequestHandler = clientRequestHandler;

	}

	@Override
	public CLIENT_TYPE login(String username, String password) throws InvalidParameter, CriticalError, EmployeeAlreadyConnected, AuthenticationError {
		CommandWrapper commandDescriptor = null;
		
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);

		log.info("Creating login command wrapper with username: " + username + " and password: " + password);
		
		String serverResponse = sendRequestWithRespondToServer((new CommandWrapper(WorkerDefs.loginCommandSenderId,
				CommandDescriptor.LOGIN, Serialization.serialize(new Login(username, password))).serialize()));
		
		try {
			commandDescriptor = CommandWrapper.deserialize(serverResponse);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | UnknownSenderID | EmployeeNotConnected |
				 ProductNotExistInCatalog | ProductAlreadyExistInCatalog |
				 ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		clientId = commandDescriptor.getSenderID();
		this.username = username;
		this.password = password;
		
		log.info("Login to server as " + commandDescriptor.getData() + " succeed. Client id is: " + clientId);
		
		terminateCommunication();
		
		return CLIENT_TYPE.deserialize(commandDescriptor.getData());
	}

	@Override
	public void logout() throws InvalidParameter, UnknownSenderID,
	CriticalError, EmployeeNotConnected {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating logout command wrapper with username: " + username);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.LOGOUT, Serialization.serialize(username)))
						.serialize());
		
		try {
			resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected |
				 AuthenticationError | ProductNotExistInCatalog |
			 	 ProductAlreadyExistInCatalog | ProductStillForSale |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("logout from server succeed.");
				
		terminateCommunication();
	}

	@Override
	public CatalogProduct viewProductFromCatalog(int barcode) throws InvalidParameter,
	UnknownSenderID, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating viewProductFromCatalog command wrapper with barcode: " + barcode);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(new SmartCode(barcode, null))).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected |
				 AuthenticationError | ProductAlreadyExistInCatalog |
				 ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("viewProductFromCatalog command succeed.");
		
		terminateCommunication();
		
		return Serialization.deserialize(commandDescriptor.getData(), CatalogProduct.class);
	}

	@Override
	public void addProductToWarehouse(ProductPackage p) throws InvalidParameter,
		UnknownSenderID, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating addProductToWarehouse command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
						Serialization.serialize(p)).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError |
				ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("addProductToWarehouse command succeed.");
		
		terminateCommunication();
	}
	
	@Override
	public void placeProductPackageOnShelves(ProductPackage p) throws InvalidParameter,
		UnknownSenderID, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog,
		AmountBiggerThanAvailable, ProductPackageDoesNotExist {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating placeProductPackageOnShelves command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES,
						Serialization.serialize(p)).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError
				| ProductAlreadyExistInCatalog | ProductStillForSale e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("placeProductPackageOnShelves command succeed.");
		
		terminateCommunication();
	}
	
	@Override
	public void removeProductPackageFromStore(ProductPackage p) throws InvalidParameter,
		UnknownSenderID, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog,
		AmountBiggerThanAvailable, ProductPackageDoesNotExist {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating removeProductPackageFromStore command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
						Serialization.serialize(p)).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError
				| ProductAlreadyExistInCatalog | ProductStillForSale e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("removeProductPackageFromStore command succeed.");
		
		terminateCommunication();
	}
	
	@Override
	public int getProductPackageAmount(ProductPackage p) throws InvalidParameter,
		UnknownSenderID, CriticalError, EmployeeNotConnected, ProductPackageDoesNotExist {		
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating getProductPackageAmount command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
						Serialization.serialize(p)).serialize()));
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError |
				 ProductNotExistInCatalog | ProductAlreadyExistInCatalog |
				 ProductStillForSale | AmountBiggerThanAvailable e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("getProductPackageAmount command succeed.");
		
		terminateCommunication();
		
		return Serialization.deserialize(commandDescriptor.getData(), Integer.class);
	}
	
	@Override
	public Login getWorkerLoginDetails() {
		return new Login(username, password);
	}
}
