package EmployeeImplementations;

import java.net.SocketTimeoutException;

import BasicCommonClasses.ProductPackage;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeContracts.IManager;
import EmployeeDefs.WorkerDefs;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * Manager - This class represent the manager functionality implementation.
 * 
 * @author Aviad Cohen
 * @since 2016-12-27
 */

public class Manager extends Worker implements IManager {

	public Manager(IClientRequestHandler clientRequestHandler) {
		super(clientRequestHandler);
	}

	@Override
	public void addProductToCatalog(ProductPackage p) throws InvalidParameter,
		CriticalError, EmployeeNotConnected, ProductNotExistInCatalog {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating addProductToCatalog command wrapper with product package: " + p);
		
		String serverResponse;
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(clientId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(p))).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		terminateCommunication();
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductAlreadyExistInCatalog
				| ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		log.info("addProductToCatalog command succeed.");
	}

	@Override
	public void removeProductFromCatalog(ProductPackage p) throws InvalidParameter,
		CriticalError, EmployeeNotConnected, ProductAlreadyExistInCatalog, ProductStillForSale {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating removeProductFromCatalog command wrapper with product package: " + p);
		
		String serverResponse;
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(clientId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(p))).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		terminateCommunication();
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductNotExistInCatalog | 
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("removeProductFromCatalog command succeed.");
	}

	@Override
	public void editProductFromCatalog(ProductPackage p) throws InvalidParameter,
		CriticalError, EmployeeNotConnected, ProductNotExistInCatalog,
		ProductAlreadyExistInCatalog {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating editProductFromCatalog command wrapper with product package: " + p);
		
		String serverResponse;
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(clientId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
							Serialization.serialize(p))).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		terminateCommunication();

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError |
				 ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("editProductFromCatalog command succeed.");
	}
}
