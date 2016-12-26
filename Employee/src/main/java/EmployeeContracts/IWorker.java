package EmployeeContracts;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
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
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	void logout() throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected;
	
	/**
	 * gets catalog product by barcode
	 * @param barcode
	 * @return
	 * @throws ProductNotExistInCatalog 
	 * @throws WorkerNotConnected 
	 * @throws CriticalError 
	 * @throws UnknownSenderID 
	 * @throws InvalidParameter 
	 */
	CatalogProduct viewProductFromCatalog(int barcode) throws InvalidParameter, UnknownSenderID, CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

}
