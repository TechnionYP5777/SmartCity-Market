package EmployeeContracts;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import CommonDefs.CLIENT_TYPE;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;

/** IWorker - This interface represent the Worker client functionality.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen
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
	 * @return CLIENT_TYPE - the client type
	 * @throws AuthenticationError 
	 * @throws EmployeeAlreadyConnected 
	 * @throws CriticalError 
	 * @throws InvalidParameter 
	 */
	CLIENT_TYPE login(String username, String password) throws InvalidParameter, CriticalError, EmployeeAlreadyConnected, AuthenticationError;
	
	/**
	 * logout method use for the worker to logout from the server.
	 * 
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void logout() throws InvalidParameter, CriticalError, EmployeeNotConnected;
	
	/**
	 * gets catalog product by barcode
	 * 
	 * @param barcode
	 * @return CatalogProduct
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	CatalogProduct viewProductFromCatalog(int barcode) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog;

	/**
	 * worker add product package to warehouse.
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void addProductToWarehouse(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog;
	
	/**
	 * worker move product package from warehouse to shelves.
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws AmountBiggerThanAvailable 
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ProductPackageDoesNotExist 
	 */
	void placeProductPackageOnShelves(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, AmountBiggerThanAvailable, ProductPackageDoesNotExist;
	
	/**
	 * worker remove product package from store (warehouse or shelves).
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws AmountBiggerThanAvailable 
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ProductPackageDoesNotExist 
	 */
	void removeProductPackageFromStore(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, AmountBiggerThanAvailable, ProductPackageDoesNotExist;
	
	/**
	 * worker get amount of ProductPackage from store (warehouse or shelves).
	 * 
	 * @param ProductPackage
	 * @return amount 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ProductPackageDoesNotExist 
	 */
	int getProductPackageAmount(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductPackageDoesNotExist;
}
