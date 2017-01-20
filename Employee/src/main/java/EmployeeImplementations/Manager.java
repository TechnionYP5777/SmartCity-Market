package EmployeeImplementations;

import javax.inject.Singleton;

import com.google.inject.Inject;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
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

@Singleton
public class Manager extends Worker implements IManager {

	@Inject
	public Manager(IClientRequestHandler clientRequestHandler) {
		super(clientRequestHandler);
	}

	@Override
	public void addProductToCatalog(CatalogProduct p) throws InvalidParameter, CriticalError, EmployeeNotConnected,
			ProductAlreadyExistInCatalog, ConnectionFailure {

		log.info("Creating addProductToCatalog command wrapper with product: " + p);

		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG, Serialization.serialize(p)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductNotExistInCatalog ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("addProductToCatalog command succeed.");
	}

	@Override
	public void removeProductFromCatalog(SmartCode c) throws InvalidParameter, CriticalError, EmployeeNotConnected,
			ProductNotExistInCatalog, ProductStillForSale, ConnectionFailure {

		log.info("Creating removeProductFromCatalog command wrapper with barcode: " + c.getBarcode());
		String serverResponse = sendRequestWithRespondToServer((new CommandWrapper(clientId,
				CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG, Serialization.serialize(c))).serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | ProductAlreadyExistInCatalog ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}

		log.info("removeProductFromCatalog command succeed.");
	}

	@Override
	public void editProductFromCatalog(CatalogProduct p)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure {

		log.info("Creating editProductFromCatalog command wrapper with product: " + p);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG, Serialization.serialize(p)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}

		log.info("editProductFromCatalog command succeed.");
	}
}
