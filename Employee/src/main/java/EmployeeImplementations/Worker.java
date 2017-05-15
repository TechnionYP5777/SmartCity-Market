package EmployeeImplementations;

import javax.inject.Singleton;

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
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ManfacturerStillInUse;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.IngredientStillInUse;
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

@Singleton
public class Worker extends AEmployee implements IWorker {

	@Inject
	public Worker(IClientRequestHandler clientRequestHandler) {
		this.clientRequestHandler = clientRequestHandler;
	}

	public CommandWrapper getCommandWrapper(String serverResponse) throws CriticalError {
		try {
			return CommandWrapper.deserialize(serverResponse);
		} catch (Exception e) {
			log.fatal("Critical bug: failed to desirealize server respond: " + serverResponse);
			
			throw new CriticalError();
		}
	}
	
	@Override
	public CLIENT_TYPE login(String username, String password, boolean updateProductPictures)
			throws InvalidParameter, CriticalError, EmployeeAlreadyConnected, AuthenticationError, ConnectionFailure {
		CommandWrapper $ = null;
		log.info("Creating login command wrapper with username: " + username + " and password: " + password);
		String serverResponse = sendRequestWithRespondToServer(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_EMPLOYEE,
				Serialization.serialize(new Login(username, password))).serialize());

		try {
			$ = getCommandWrapper(serverResponse);
			
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeNotConnected | ProductNotExistInCatalog
				| ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | 
				WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		setClientId($.getSenderID());
		this.username = username;
		this.password = password;
		log.info("Login to server as " + $.getData() + " succeed. Client id is: " + getClientId());

		if (updateProductPictures){
			UpdateProductPictures UpdateProductPicturesThread = new UpdateProductPictures();
			UpdateProductPicturesThread.start();
		}

		return CLIENT_TYPE.deserialize($.getData());
	}

	@Override
	public void logout() throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure {
		log.info("Creating logout command wrapper with username: " + username);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.LOGOUT, Serialization.serialize(username)))
						.serialize());
		try {
			resultDescriptorHandler(getCommandWrapper(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductNotExistInCatalog
				| ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | 
				WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		setClientId(WorkerDefs.loginCommandSenderId);
		log.info("logout from server succeed.");
	}

	@Override
	public boolean isLoggedIn() throws CriticalError, ConnectionFailure {
		String serverResponse;

		log.info("Creating is logged in command wrapper with senderID: " + getClientId());

		serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.IS_LOGGED_IN)).serialize());

		CommandWrapper commandWrapper = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | EmployeeNotConnected | EmployeeAlreadyConnected
				| AuthenticationError | ProductNotExistInCatalog | ProductAlreadyExistInCatalog | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | WorkerAlreadyExists | ParamIDAlreadyExists |
				ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("is logged out from server succeed");

		return Serialization.deserialize(commandWrapper.getData(), Boolean.class);
	}

	@Override
	public CatalogProduct viewProductFromCatalog(long barcode)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure {
		log.info("Creating viewProductFromCatalog command wrapper with barcode: " + barcode);
		String serverResponse = sendRequestWithRespondToServer(
				new CommandWrapper(getClientId(), CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(new SmartCode(barcode, null))).serialize());

		CommandWrapper $ = getCommandWrapper(serverResponse);
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError
				| ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | 
				WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		log.info("viewProductFromCatalog command succeed.");
		return Serialization.deserialize($.getData(), CatalogProduct.class);
	}

	@Override
	public void addProductToWarehouse(ProductPackage p)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure {
		log.info("Creating addProductToWarehouse command wrapper with product package: " + p);
		String serverResponse = sendRequestWithRespondToServer(new CommandWrapper(getClientId(), CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
				Serialization.serialize(p)).serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError
				| ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist |
				WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		log.info("addProductToWarehouse command succeed.");
	}

	@Override
	public void placeProductPackageOnShelves(ProductPackage p)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog,
			AmountBiggerThanAvailable, ProductPackageDoesNotExist, ConnectionFailure {
		log.info("Creating placeProductPackageOnShelves command wrapper with product package: " + p);
		String serverResponse = sendRequestWithRespondToServer(new CommandWrapper(getClientId(), CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES,
				Serialization.serialize(p)).serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError
				| ProductAlreadyExistInCatalog | ProductStillForSale | WorkerAlreadyExists | ParamIDAlreadyExists | 
				ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		log.info("placeProductPackageOnShelves command succeed.");
	}

	@Override
	public void removeProductPackageFromStore(ProductPackage p)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog,
			AmountBiggerThanAvailable, ProductPackageDoesNotExist, ConnectionFailure {
		log.info("Creating removeProductPackageFromStore command wrapper with product package: " + p);
		String serverResponse = sendRequestWithRespondToServer(new CommandWrapper(getClientId(), CommandDescriptor.REMOVE_PRODUCT_PACKAGE_FROM_STORE,
				Serialization.serialize(p)).serialize());
		
		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);
		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError
				| ProductAlreadyExistInCatalog | ProductStillForSale | WorkerAlreadyExists | ParamIDAlreadyExists | 
				ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		log.info("removeProductPackageFromStore command succeed.");
	}

	@Override
	public int getProductPackageAmount(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected,
			ProductPackageDoesNotExist, ConnectionFailure {
		log.info("Creating getProductPackageAmount command wrapper with product package: " + p);
		String serverResponse = sendRequestWithRespondToServer(
				new CommandWrapper(getClientId(), CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
						Serialization.serialize(p)).serialize());

		CommandWrapper $ = getCommandWrapper(serverResponse);
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductNotExistInCatalog
				| ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable | WorkerAlreadyExists |
				ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		log.info("getProductPackageAmount command succeed.");
		return Serialization.deserialize($.getData(), Integer.class);
	}

	@Override
	public Login getWorkerLoginDetails() {
		return new Login(username, password);
	}
}
