package EmployeeImplementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Login;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.Sale;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeContracts.IManager;
import SMExceptions.CommonExceptions.CriticalError;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ManfacturerStillInUse;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeDefs.AEmployeeException.ParamIDStillInUse;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import EmployeeDefs.WorkerDefs;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.IngredientStillInUse;
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
		this.username = WorkerDefs.managerDefaultUsername;
	}

	@Override
	public void addProductToCatalog(CatalogProduct p) throws InvalidParameter, CriticalError, EmployeeNotConnected,
			ProductAlreadyExistInCatalog, ConnectionFailure {

		log.info("Creating addProductToCatalog command wrapper with product: " + p);

		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.ADD_PRODUCT_TO_CATALOG, Serialization.serialize(p)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductNotExistInCatalog | WorkerAlreadyExists 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		log.info("addProductToCatalog command succeed.");
	}

	@Override
	public void removeProductFromCatalog(SmartCode c) throws InvalidParameter, CriticalError, EmployeeNotConnected,
			ProductNotExistInCatalog, ProductStillForSale, ConnectionFailure {

		log.info("Creating removeProductFromCatalog command wrapper with barcode: " + c.getBarcode());
		String serverResponse = sendRequestWithRespondToServer((new CommandWrapper(getClientId(),
				CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG, Serialization.serialize(c))).serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | WorkerAlreadyExists | ParamIDAlreadyExists 
				| ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("removeProductFromCatalog command succeed.");
	}

	@Override
	public void editProductFromCatalog(CatalogProduct p)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure {

		log.info("Creating editProductFromCatalog command wrapper with product: " + p);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG, Serialization.serialize(p)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | WorkerAlreadyExists 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("editProductFromCatalog command succeed.");
	}

	@Override
	public void registerNewWorker(Login l)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, WorkerAlreadyExists {
		log.info("Creating registerNewWorker command wrapper with username: " + l.getUserName());
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.REGISTER_NEW_WORKER, Serialization.serialize(l)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("registerNewWorker command succeed.");
	}

	@Override
	public void removeWorker(String s)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, WorkerDoesNotExist {
		log.info("Creating removeWorker command wrapper with username: " + s);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.REMOVE_WORKER, Serialization.serialize(s)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
				| ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerAlreadyExists | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("removeWorker command succeed.");
		
	}

	@Override
	public Ingredient addIngredient(Ingredient w)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists {
		log.info("Creating addIngredient command wrapper with Ingredient: " + w);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.ADD_INGREDIENT, Serialization.serialize(w)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | ParamIDDoesNotExist | WorkerAlreadyExists | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("addIngredient command succeed.");	
		
		return Serialization.deserialize(commandDescriptor.getData(), Ingredient.class);
	}

	@Override
	public void removeIngredient(Ingredient w, Boolean forced)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist, IngredientStillInUse {
		log.info("Creating removeIngredient command wrapper with Ingredient: " + w);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), !forced ? CommandDescriptor.REMOVE_INGREDIENT : CommandDescriptor.FORCE_REMOVE_INGREDIENT, Serialization.serialize(w)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("removeIngredient command succeed.");	
	}

	@Override
	public void editIngredient(Ingredient w)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist {
		log.info("Creating editIngredient command wrapper with Ingredient: " + w);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.EDIT_INGREDIENT, Serialization.serialize(w)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("editIngredient command succeed.");	
	}
	
	@Override
	public Manufacturer addManufacturer(Manufacturer m)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists {
		log.info("Creating addManufacturer command wrapper with Manufacturer: " + m);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.ADD_MANUFACTURER, Serialization.serialize(m)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("addManufacturer command succeed.");
		
		return Serialization.deserialize(commandDescriptor.getData(), Manufacturer.class);
	}

	@Override
	public void removeManufacturer(Manufacturer m)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist, ManfacturerStillInUse {
		log.info("Creating removeManufacturer command wrapper with Manufacturer: " + m);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.REMOVE_MANUFACTURER, Serialization.serialize(m)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist | IngredientStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("removeManufacturer command succeed.");		
	}

	@Override
	public void editManufacturer(Manufacturer m)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist {
		log.info("Creating editManufacturer command wrapper with Manufacturer: " + m);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.EDIT_MANUFACTURER, Serialization.serialize(m)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("editManufacturer command succeed.");		
	}
	
	@Override
	public List<Manufacturer> getAllManufacturers()
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure {
		log.info("Creating getAllManufacturers command wrapper");
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.GET_ALL_MANUFACTURERS, Serialization.serialize("")))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}
		
		log.info("getAllManufacturers command succeed.");
		
		return new Gson().fromJson(commandDescriptor.getData(), new TypeToken<List<Manufacturer>>(){}.getType());
	}

	@Override
	public List<Ingredient> getAllIngredients()
			throws InvalidParameter, CriticalError, ConnectionFailure {
		log.info("Creating getAllIngredients command wrapper");
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.GET_ALL_INGREDIENTS, Serialization.serialize("")))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse | EmployeeNotConnected | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}
		
		log.info("getAllIngredients command succeed.");
		
		return new Gson().fromJson(commandDescriptor.getData(), new TypeToken<List<Ingredient>>(){}.getType());
	}
	
	@Override
	public HashMap<String, Boolean> getAllWorkers()
			throws CriticalError, EmployeeNotConnected, ConnectionFailure {
		log.info("Creating getAllWorkers command wrapper");
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.GET_ALL_WORKERS, Serialization.serialize("")))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse |
			    InvalidParameter | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();		}
		
		log.info("getAllWorkers command succeed.");
		
		return new Gson().fromJson(commandDescriptor.getData(), new TypeToken<HashMap<String, Boolean>>(){}.getType());
	}

	@Override
	public Sale createNewSale(Sale sale)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists {
		log.info("Creating createNewSale command wrapper with Sale: " + sale);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.CREATE_NEW_SALE, Serialization.serialize(sale)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse |
			    ParamIDAlreadyExists | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("createNewSale command succeed.");
		
		return Serialization.deserialize(commandDescriptor.getData(), Sale.class);
	}

	@Override
	public void removeSale(Integer saleID)
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist, ParamIDStillInUse {
		log.info("Creating removeSale command wrapper with Sale id: " + saleID);
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.REMOVE_SALE, Serialization.serialize(saleID)))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();
		}

		log.info("removeSale command succeed.");
	}

	@Override
	public Map<Integer, Sale> getAllSales()
			throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure {
		log.info("Creating getAllSales command wrapper");
		String serverResponse = sendRequestWithRespondToServer(
				(new CommandWrapper(getClientId(), CommandDescriptor.GET_ALL_SALES, Serialization.serialize("")))
						.serialize());

		CommandWrapper commandDescriptor = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandDescriptor.getResultDescriptor());
		} catch (InvalidCommandDescriptor | EmployeeAlreadyConnected | AuthenticationError | ProductStillForSale
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductAlreadyExistInCatalog | ProductNotExistInCatalog 
			    | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist | WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse |
			    InvalidParameter | ParamIDStillInUse ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			throw new CriticalError();		}
		
		log.info("getAllSales command succeed.");
		
		return new Gson().fromJson(commandDescriptor.getData(), new TypeToken<HashMap<Integer, Sale>>(){}.getType());
	}
}
