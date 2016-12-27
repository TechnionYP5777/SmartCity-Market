package EmployeeContracts;

import BasicCommonClasses.ProductPackage;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.ProductStillForSale;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.AEmployeeExceptions.EmployeeNotConnected;

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
	 * @throws ProductNotExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void addProductToCatalog(ProductPackage p) throws InvalidParameter, UnknownSenderID, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog;
	
	/**
	 * Manager removes product to catalog.
	 * 
	 * @param ProductPackage
	 * @return amount 
	 * @throws ProductStillForSale 
	 * @throws ProductAlreadyExistInCatalog 
	 * @throws EmployeeNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void removeProductFromCatalog(ProductPackage p) throws InvalidParameter, UnknownSenderID, CriticalError, EmployeeNotConnected, ProductAlreadyExistInCatalog, ProductStillForSale;
	
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
	 */
	void editProductFromCatalog(ProductPackage p) throws InvalidParameter, UnknownSenderID, CriticalError, EmployeeNotConnected, ProductNotExistInCatalog, ProductAlreadyExistInCatalog;
}
