package EmployeeContracts;

import BasicCommonClasses.ProductPackage;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;

/** IManager - This interface represent the manager functionality which is a worker.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen
 * @since 2016-12-17 */

public interface IManager extends IWorker {
	/**
	 * Manager adds product to catalog.
	 * 
	 * @param ProductPackage
	 * @return void
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ProductAlreadyExistInCatalog 
	 * @throws ConnectionFailure 
	 */
	void addProductToCatalog(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductAlreadyExistInCatalog, ConnectionFailure;
	
	/**
	 * Manager removes product to catalog.
	 * 
	 * @param ProductPackage
	 * @return amount 
	 * @throws ProductStillForSale 
 	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	void removeProductFromCatalog(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductStillForSale, ProductNotExistInCatalog, ConnectionFailure;
	
	/**
	 * Manager edits product to catalog.
	 * 
	 * @param ProductPackage
	 * @return amount 
	 * @throws ProductAlreadyExistInCatalog 
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 * @throws ConnectionFailure 
	 */
	void editProductFromCatalog(ProductPackage p) throws InvalidParameter, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ConnectionFailure;
}
