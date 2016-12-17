package EmployeeContracts;

/** IWorker - This interface represent the Worker client functionality.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17 */

public interface IWorker {
	
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
	
	// TODO - SHOULD BE INT OR LONG?
	int viewProductFromCatalog();

}
