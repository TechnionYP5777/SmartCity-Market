package EmployeeImplementations;

import java.util.List;

import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Login;
import BasicCommonClasses.Manufacturer;
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
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductNotExistInCatalog | WorkerAlreadyExists 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
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
				| ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | WorkerAlreadyExists | ParamIDAlreadyExists 
				| ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
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
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | WorkerAlreadyExists 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("editProductFromCatalog command succeed.");
	}

	@Override
	public void registerNewWorker(Login l)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, WorkerAlreadyExists {
		log.info("Creating registerNewWorker command wrapper with username: " + l.getUserName());
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.REGISTER_NEW_WORKER, Serialization.serialize(l)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("registerNewWorker command succeed.");
	}

	@Override
	public void removeWorker(String s)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, WorkerDoesNotExist {
		log.info("Creating removeWorker command wrapper with username: " + s);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.REMOVE_WORKER, Serialization.serialize(s)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("removeWorker command succeed.");
		
	}

	@Override
	public void addIngredient(Ingredient w)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists {
		log.info("Creating addIngredient command wrapper with Ingredient: " + w);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.ADD_INGREDIENT, Serialization.serialize(w)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | ParamIDDoesNotExist | WorkerAlreadyExists | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			;
		}

		log.info("addIngredient command succeed.");		
	}

	@Override
	public void removeIngredient(Ingredient w)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist {
		log.info("Creating removeIngredient command wrapper with Ingredient: " + w);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.REMOVE_INGREDIENT, Serialization.serialize(w)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("removeIngredient command succeed.");	
	}

	@Override
	public void editIngredient(Ingredient w)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist {
		log.info("Creating editIngredient command wrapper with Ingredient: " + w);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.EDIT_INGREDIENT, Serialization.serialize(w)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("editIngredient command succeed.");	
	}
	
	@Override
	public void addManufacturer(Manufacturer m)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists {
		log.info("Creating addManufacturer command wrapper with Manufacturer: " + m);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.ADD_MANUFACTURER, Serialization.serialize(m)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("addManufacturer command succeed.");
	}

	@Override
	public void removeManufacturer(Manufacturer m)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist {
		log.info("Creating removeManufacturer command wrapper with Manufacturer: " + m);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.REMOVE_MANUFACTURER, Serialization.serialize(m)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("removeManufacturer command succeed.");		
	}

	@Override
	public void editManufacturer(Manufacturer m)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist {
		log.info("Creating editManufacturer command wrapper with Manufacturer: " + m);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.EDIT_MANUFACTURER, Serialization.serialize(m)))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}

		log.info("editManufacturer command succeed.");		
	}
	
	@Override
	public List<Manufacturer> getAllManufacturers()
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure {
		log.info("Creating getAllManufacturers command wrapper");
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.GET_ALL_MANUFACTURERS, Serialization.serialize("")))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			return null;
		}
		
		log.info("getAllManufacturers command succeed.");
		
		return new Gson().fromJson(commandDescriptor.getData(), new TypeToken<List<Manufacturer>>(){}.getType());
	}

	@Override
	public List<Ingredient> getAllIngredients()
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure {
		log.info("Creating getAllIngredients command wrapper");
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(clientId, CommandDescriptor.GET_ALL_INGREDIENTS, Serialization.serialize("")))
						.serialize());

		CommandWrapper commandDescriptor = CommandWrapper.deserialize(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			return null;
		}
		
		log.info("getAllIngredients command succeed.");
		
		return new Gson().fromJson(commandDescriptor.getData(), new TypeToken<List<Ingredient>>(){}.getType());
	}
}
