package EmployeeContracts;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;

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
	 */
	void login(String username, String password);
	
	/**
	 * logout method use for the worker to logout from the server.
	 */
	void logout();
	
	/**
	 * gets catalog product by barcode
	 * @param barcode
	 * @return
	 */
	CatalogProduct viewProductFromCatalog(int barcode);

}
