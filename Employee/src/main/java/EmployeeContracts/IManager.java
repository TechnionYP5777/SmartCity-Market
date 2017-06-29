package EmployeeContracts;

import java.util.List;
import java.util.Map;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Login;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.Sale;
import BasicCommonClasses.SmartCode;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ManfacturerStillInUse;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeDefs.AEmployeeException.ParamIDStillInUse;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import SMExceptions.CommonExceptions.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.IngredientStillInUse;

/** IManager - This interface represent the manager functionality which is a worker.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen
 * @since 2016-12-17 */

public interface IManager extends IWorker {
	/**
	 * Manager adds product to catalog.
	 * 
	 * @param CatalogProduct
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ProductAlreadyExistInCatalog 
	 * @throws ConnectionFailure 
	 */
	void addProductToCatalog(CatalogProduct p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductAlreadyExistInCatalog, ConnectionFailure;
	
	/**
	 * Manager removes product to catalog.
	 * 
	 * @param SmartCode
	 * @return void
	 * @throws ProductStillForSale 
 	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	void removeProductFromCatalog(SmartCode c) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductStillForSale, ProductNotExistInCatalog, ConnectionFailure;
	
	/**
	 * Manager edits product to catalog.
	 * 
	 * @param CatalogProduct
	 * @return void
	 * @throws ProductAlreadyExistInCatalog 
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	void editProductFromCatalog(CatalogProduct p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure;
	
	/**
	 * Manager register new worker.
	 * 
	 * @param Login
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws WorkerAlreadyExists 
	 */
	void registerNewWorker(Login l) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, WorkerAlreadyExists;
	
	/**
	 * Manager remove worker.
	 * 
	 * @param String
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws WorkerDoesNotExist 
	 */
	void removeWorker(String s) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, WorkerDoesNotExist;
	
	/**
	 * Manager add new Ingredient.
	 * 
	 * @param Ingredient
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws ParamIDAlreadyExists 
	 */
	Ingredient addIngredient(Ingredient w) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists;
	
	/**
	 * Manager remove Ingredient.
	 * 
	 * @param Ingredient
	 * @param forced - force remove the ingredient from system
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws ParamIDDoesNotExist 
	 * @throws IngredientStillInUse 
	 */
	void removeIngredient(Ingredient w, Boolean forced) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist, IngredientStillInUse;
	
	/**
	 * Manager add new Manufacturer.
	 * 
	 * @param Manufacturer
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws ParamIDAlreadyExists 
	 */
	Manufacturer addManufacturer(Manufacturer m) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDAlreadyExists;
	
	/**
	 * Manager remove Manufacturer.
	 * 
	 * @param Manufacturer
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws ParamIDDoesNotExist 
	 * @throws ManfacturerStillInUse 
	 */
	void removeManufacturer(Manufacturer w) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist, ManfacturerStillInUse;
	
	/**
	 * Manager get list of all Manufacturers.
	 * 
	 * @param void
	 * @return List of Manufacturer
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	List<Manufacturer> getAllManufacturers() throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure;
	
	/**
	 * Manager get list of all Ingredients.
	 * 
	 * @param void
	 * @return List of Ingredient
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	List<Ingredient> getAllIngredients() throws InvalidParameter, CriticalError, ConnectionFailure;

	/**
	 * Manager edit Manufacturer.
	 * 
	 * @param Manufacturer
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws ParamIDDoesNotExist 
	 */
	void editManufacturer(Manufacturer m) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist;

	/**
	 * Manager edit Manufacturer.
	 * 
	 * @param Manufacturer
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 * @throws ParamIDDoesNotExist 
	 */
	void editIngredient(Ingredient w) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist;

	/**
	 * Manager edit Manufacturer.
	 * 
	 * @param void
	 * @return List<String>
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws ConnectionFailure 
	 */
	Map<String, Boolean> getAllWorkers() throws CriticalError, EmployeeNotConnected, ConnectionFailure;
	
	/**
	 * Manager creates new sale..
	 * 
	 * @param sale - sale to create
	 * @return Integer - Sale with ID
	 * @throws EmployeeNotConnected 
	 * @throws InvalidParameter 
	 * @throws CriticalError 
	 * @throws ConnectionFailure 
	 * @throws ParamIDAlreadyExists 
	 */

	Integer createNewSale(Sale sale) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure;
	
	/**
	 * Manager removes sale..
	 * 
	 * @param sale - sale to create
	 * @return Integer - Sale with ID
	 * @throws EmployeeNotConnected 
	 * @throws InvalidParameter 
	 * @throws CriticalError 
	 * @throws ConnectionFailure 
	 * @throws ParamIDDoesNotExist 
	 * @throws ParamIDStillInUse 
	 */
	void removeSale(Integer saleID) throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure, ParamIDDoesNotExist, ParamIDStillInUse;
	
	/**
	 * Manager get all sales.
	 * 
	 * @param void
	 * @return Map<Integer, Sale> 
	 * @throws EmployeeNotConnected 
	 * @throws InvalidParameter 
	 * @throws CriticalError 
	 * @throws ConnectionFailure 
	 */
	List<Sale> getAllSales() throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure;
}
