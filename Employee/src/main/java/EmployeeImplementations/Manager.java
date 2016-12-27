package EmployeeImplementations;

import BasicCommonClasses.ProductPackage;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeContracts.IManager;
import EmployeeDefs.WorkerDefs;
import EmployeeDefs.AEmployeeExceptions.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeExceptions.AuthenticationError;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeExceptions.ProductStillForSale;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.AEmployeeExceptions.WorkerAlreadyConnected;
import EmployeeDefs.AEmployeeExceptions.WorkerNotConnected;
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
		UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating addProductToCatalog command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
						Serialization.serialize(p))).serialize());
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | WorkerAlreadyConnected | AuthenticationError | ProductAlreadyExistInCatalog
				| ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("addProductToCatalog command succeed.");
		
		terminateCommunication();
	}

	@Override
	public void removeProductFromCatalog(ProductPackage p) throws InvalidParameter,
		UnknownSenderID, CriticalError, WorkerNotConnected, ProductAlreadyExistInCatalog, ProductStillForSale {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating removeProductFromCatalog command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						Serialization.serialize(p))).serialize());
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | WorkerAlreadyConnected | AuthenticationError | ProductNotExistInCatalog | 
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("removeProductFromCatalog command succeed.");
		
		terminateCommunication();
	}

	@Override
	public void editProductFromCatalog(ProductPackage p) throws InvalidParameter,
		UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog,
		ProductAlreadyExistInCatalog {
		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		
		log.info("Creating editProductFromCatalog command wrapper with product package: " + p);
		
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
						Serialization.serialize(p))).serialize());
		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | WorkerAlreadyConnected | AuthenticationError |
				 ProductStillForSale | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			e.printStackTrace();
		}
		
		log.info("editProductFromCatalog command succeed.");
		
		terminateCommunication();		
	}
}
