package EmployeeContracts;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import EmployeeDefs.AEmployeeExceptions.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeExceptions.AuthenticationError;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.AEmployeeExceptions.WorkerAlreadyConnected;
import EmployeeDefs.AEmployeeExceptions.WorkerNotConnected;

/** IWorker - This interface represent the Worker client functionality.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17 */

public interface IWorker {
		
	/**
	 * 
	 * @return username and password in holding class LoginParameters
	 */
	Login getWorkerLoginDetails();
	
	/**
	 * login method use for the worker to login to the server.
	 * 
	 * @param username - worker username
	 * @param password - worker password
	 * @throws AuthenticationError 
	 * @throws WorkerAlreadyConnected 
	 * @throws CriticalError 
	 * @throws InvalidParameter 
	 */
	void login(String username, String password) throws InvalidParameter, CriticalError, WorkerAlreadyConnected, AuthenticationError;
	
	/**
	 * logout method use for the worker to logout from the server.
	 * 
	 * @return void
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void logout() throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected;
	
	/**
	 * gets catalog product by barcode
	 * 
	 * @param barcode
	 * @return CatalogProduct
	 * @throws ProductNotExistInCatalog 
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	CatalogProduct viewProductFromCatalog(int barcode) throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	/**
	 * worker add product package to warehouse.
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws ProductNotExistInCatalog 
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void addProductToWarehouse(ProductPackage p) throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog;
	
	/**
	 * worker move product package from warehouse to shelves.
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws AmountBiggerThanAvailable 
	 * @throws ProductNotExistInCatalog 
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void placeProductPackageOnShelves(ProductPackage p) throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog, AmountBiggerThanAvailable;
	
	/**
	 * worker remove product package from store (warehouse or shelves).
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws AmountBiggerThanAvailable 
	 * @throws ProductNotExistInCatalog 
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void removeProductPackageFromStore(ProductPackage p) throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog, AmountBiggerThanAvailable;
}
