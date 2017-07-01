package EmployeeContracts;

import java.util.HashSet;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import CommonDefs.CLIENT_TYPE;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import SMExceptions.CommonExceptions.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;

/** IWorker - This interface represent the Worker client functionality.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen
 * @since 2016-12-17 
 *
 */
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
	 * @param updateProductPictures - when set true, UpdateProductPictures flow is activated. By default it should be false.
	 * @return CLIENT_TYPE - the client type
	 * @throws AuthenticationError 
	 * @throws EmployeeAlreadyConnected 
	 * @throws CriticalError 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	CLIENT_TYPE login(String username, String password, boolean updateProductPictures) throws InvalidParameter, CriticalError, EmployeeAlreadyConnected, AuthenticationError, ConnectionFailure;
	
	/**
	 * logout method use for the worker to logout from the server.
	 * 
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	void logout() throws InvalidParameter, CriticalError, EmployeeNotConnected, ConnectionFailure;
	
	
	
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
	 * @throws ConnectionFailure 
	 */
	CatalogProduct viewProductFromCatalog(long barcode) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure;

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
	 * @throws ConnectionFailure 
	 */
	void addProductToWarehouse(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure;
	
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
	 * @throws ConnectionFailure 
	 */
	void placeProductPackageOnShelves(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, AmountBiggerThanAvailable, ProductPackageDoesNotExist, ConnectionFailure;
	
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
	 * @throws ConnectionFailure 
	 */
	void removeProductPackageFromStore(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, AmountBiggerThanAvailable, ProductPackageDoesNotExist, ConnectionFailure;
	
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
	 * @throws ConnectionFailure 
	 */
	int getProductPackageAmount(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductPackageDoesNotExist, ConnectionFailure;
	
	/**
	 * worker requests to know if he is logged in.
	 * 
	 * @return boolean 
	 * @throws CriticalError 
	 * @throws ConnectionFailure 
	 */
	boolean isLoggedIn() throws CriticalError, ConnectionFailure;

	String getUsername();
	
	
	/**
	 * worker get all expired product packages.
	 * 
	 * @return expiredProductPackages 
	 * @throws CriticalError 
	 * @throws EmployeeNotConnected 
	 * @throws ConnectionFailure 
	 */
	HashSet<ProductPackage> getAllExpiredProductPackages() throws CriticalError, EmployeeNotConnected, ConnectionFailure; 
}
