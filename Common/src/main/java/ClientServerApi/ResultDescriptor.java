package ClientServerApi;

/**
 * These our result descriptors returned by the server. Add here more if
 * needed. keep the convention!
 * 
 * @author idan atias
 * @author shimon azulay
 * @author Aviad Cohen
 * @author Lior Ben Ami
 * @since 2016-10-09 
 */
public enum ResultDescriptor {

/************************************************ Success ***************************************************/
	/**
	 * The command request accepted by the server and was executed successfully.
	 */
	SM_OK,
	
/************************************************ Failures **************************************************/

	/**************************************** General Failures **********************************************/
	
	/**
	 * The command request denied due to unsupported command by the server.
	 * 
	 * How to fix:
	 * 
	 * 	1. Report critical error (Check for bug when see such message).
	 * 	2. Try to send the command again (maybe the command failed due to network errors).
	 * 
	 */
	SM_INVALID_CMD_DESCRIPTOR,

	/**
	 * The command request has invalid parameter.
	 * The format of one of the parameters is illegal.
	 * i.e.: The client inserted negative in senderID in command which must have legal senderID.
	 * 
	 * How to fix:
	 * 
	 * 	1. Report critical error (Check for a bug when see such message) since such message
	 *     isn't supposed to be sent to the server.
	 * 
	 */
	SM_INVALID_PARAMETER,
	
	/**
	 * The command request failed due to general error (might be due to server initial failure).
	 * 
	 * How to fix:
	 * 
	 * 1. Report critical error (Check for a bug when see such message).
	 * 2. Try to send the message again (maybe the command failed due to network errors).
	 *
	 */
	SM_ERR,

	/****************************************** Login/Logout failures **************************************/
	
	/**
	 * The sender is not connected to the system but tried to execute command still.
	 * 
	 * How to fix:
	 * 
	 * 1. Report critical error (Check for a bug when see such message).
	 * 2. Ask the user to login again.
	 * 
	 */
	SM_SENDER_IS_NOT_CONNECTED,
	
	/**
	 * The sender tried to login to the server although he is already connected.
	 * 
	 * This might be caused due to:
	 * 1. Wrong sender id (which already exists on the system).
	 * 2. Synchronization problem with the server.
	 * 
	 * How to fix:
	 *
	 * 1. Report critical error (Check for a bug when see such message).
	 * 2. Ask the user to logout and login again.
	 * 
	 */
	SM_SENDER_IS_ALREADY_CONNECTED,
	
	/**
	 * The sender tried to connect to the server with unknown username or with wrong password.
	 * 
	 * Due to security reasons the user shouldn't know if the username exists in the server database
	 * or he inserted a wrong password.
	 * 
	 * How to fix:
	 * 
	 * 1. Ask the user to login again with another username/password.
	 * 
	 */
	SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD,
	
	/********************************** Shared failures for employee & customer ********************************/
	
	/**
	 * The sender tried to get invalid product.
	 * 
	 * How to fix:
	 * 
	 * 1. If the Smartcode was a valid Smartcode - please add to the server database.
	 * 2. Otherwise - report critical error (Check for a bug when see such message).
	 * 
	 */
	SM_CATALOG_PRODUCT_DOES_NOT_EXIST,
	
	/**
	 * The sender tried to register with existing username.
	 * 
	 * How to fix:
	 * 
	 * 1. Ask user to insert different username.
	 * 
	 */
	SM_USERNAME_ALREADY_EXISTS,
	
	/**
	 * The sender tried to register with existing username.
	 * 
	 * How to fix:
	 * 
	 * 1. Ask user to insert different username.
	 * 
	 */
	SM_USERNAME_DOES_NOT_EXIST,
	
	/************************************** Employee failures only *****************************************/
	
	/**
	 * The sender tried to add catalog product with existing barcode.
	 * 
	 * How to fix:
	 * 1. Report critical error (Check for a bug when see such message).
	 * 2. Tell the employee that the given barcode already exists in the market catalog and he may choose:
	 * 		a. Change the given barcode.
	 * 		b. Edit the current product using the new data.
	 * 		c. Fetch information from the database regarding the same barcode.
	 * 		d. Abort operation.
	 * 
	 */
	SM_CATALOG_PRODUCT_ALREADY_EXISTS,
	
	/**
	 * The manager tried to remove catalog product which still available for sale.
	 * 
	 * How to fix:
	 * 1. Tell the manager that the given product still available for sale and he may choose:
	 * 		a. Change the given barcode.
	 * 		b. Remove the CatalogProduct anyway and remove all existing packages of the product.
	 * 		c. Abort operation.
	 * 
	 */
	SM_CATALOG_PRODUCT_STILL_FOR_SALE,
	
	/**
	 * The amount from the selected package is not available.
	 * 
	 * How to fix:
	 * 1. Tell the customer/employee that the given amount is illegal and he may choose:
	 * 		a. Change the given amount.
	 * 		b. Abort operation.
	 * 
	 */
	SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE,

	/***************************************** customer failures only ******************************************/
	
	/**
	 * The customer tried to add product package to grocery list which dosen't exist.
	 * 
	 * How to fix:
	 * 1. Report critical error (Check for a bug when see such message).
	 * 2. Tell the customer that the given product not available for sale (or not in the grocery list) and he may choose:
	 * 		a. Change the given SmartCode.
	 * 		b. Abort operation.
	 * 
	 */
	SM_PRODUCT_PACKAGE_DOES_NOT_EXIST,
	
	/**
	 * The customer tried to checkout an empty grocery list.
	 * 
	 * How to fix:
	 * 1. Report critical error (Check for a bug when see such message) - checkout command
	 *    isn't supposed to be sent when grocery list is empty.
	 * 2. Tell the customer that the given grocery list is empty and can't be checked-out.
	 * 
	 */
	SM_GROCERY_LIST_IS_EMPTY,
	
	/**
	 * The parameter exists with the given ID
	 * 
	 * How to fix:
	 * 1. Report user that item already exists.
	 * 
	 */
	PARAM_ID_ALREADY_EXISTS,
	
	/**
	 * The parameter ID is not exist in the system
	 * 
	 * How to fix:
	 * 1. Report user that item is not exist.
	 * 
	 */
	PARAM_ID_IS_NOT_EXIST,
	
	/**
	 * The ingredient is still in use in the system
	 * 
	 * How to fix:
	 * 1. Tell the manager that the given ingredient still used in products or in customers profiles
	 * 		a. Remove the Ingredient anyway and remove all existing occurences in the system.
	 * 		b. Abort operation.
	 * 
	 */
	SM_INGREDIENT_STILL_IN_USE,
	
	/**
	 * The manufacturer is still in use in the system
	 * 
	 * How to fix:
	 * 1. Tell the manager that the given product still available for sale and he may choose:
	 * 		a. Change the given manager.
	 * 		b. Abort operation.
	 * 
	 */
	SM_MANUFACTURER_STILL_IN_USE,
	
	/**
	 * The tried to get pictures no update needed.
	 * 
	 */
	SM_NO_UPDATE_NEEDED,
}
