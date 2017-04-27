package ClientServerApi;

/**
 * ****************************************************************************************************
 *
 *  When adding a new command, make sure you add it's name & description here as well.
 *
 * ****************************************************************************************************
 * KEEP THE CONVENTIONS!
 * ****************************************************************************************************
 * ***** General Notes *****
 * 
 * 1. retval is relevant only on success (result_code = SM_OK).
 * 2. retval will be stored in the data field of the CommandWrapper (unless maintained otherwise).
 * 3. The Server/Client will interpret the data field according to the structure which is mentioned in
 *	  the data field, the data will be stored in json.
 * 4. When adding new commands, added reference in Github for all project contributes.
 * 5. *** IMPORTANT ***
 * 
 *    For each command there is also option to get one of the following ResultDescriptors:
 *    a. SM_ERR - a ResultDescriptor for internal failure in the 
 * 	  server, such case must be considered always by the client side.
 * 	  b. SM_INVALID_PARAMETER - one of the given parameter has invalid format.
 * 
 * ****************************************************************************************************
 * 
 * 
 * @author idan atias
 * @author shimon azulay
 * @author Aviad Cohen
 * @author Lior Ben Ami
 */
public enum CommandDescriptor {
	

	/******************************************** Connection **********************************************/

	//TODO Aviad - remove old LOGIN command after finishing splitting to two commands
	/**
	 * Description: Client login command to get in the system and receive unique sender id from server.
	 * param1: Login.
	 * retval: CLIENT_TYPE.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_ALREADY_CONNECTED,
	 *			SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD,
	 *
	 *	 ***** NOTES *****
	 * 	1. The sender ID returns in senderId field.
	 *  2. Customer use username & password (use "Customer" on both).
	 */
	LOGIN,

	/**
	 * Description: Employee login command to get into the system and receive unique sender id from server.
	 * param1: Login.
	 * retval: CLIENT_TYPE.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_ALREADY_CONNECTED,
	 *			SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD,
	 *
	 *	 ***** NOTE *****
	 * 	1. The sender ID returns in senderId field.
	 */
	LOGIN_EMPLOYEE,
	
	/**
	 * Description: Customer login command to get into the system and receive unique sender id and customer profile if needed from server.
	 * param1: Login.
	 * retval: CustomerProfile.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_ALREADY_CONNECTED,
	 *			SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD,
	 *
	 *	 ***** NOTES *****
	 * 	1. The sender ID returns in senderId field.
	 *  2. For Guest login use Guest as username & password (use "Guest" on both).
	 *  3. "Guest" will be saved username, the clients can't register with this username.
	 *  4. For Guest, the server will return CustomerProfile with only username and password (TODO - AVIAD: why password?) (other attributes will be null).
	 */
	LOGIN_CUSTOMER,
	
	/**
	 * Description: Client logout command for logging out of server.
	 * param1: String username.
	 * retval: void
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED
	 *
	 *	 ***** NOTES *****
	 * 	 1. This command has no data from the server side.
	 * 	 2. On Customer, the senderId will be the identification for logout and will ABORT the current GroceryList.
	 */
	LOGOUT,
	
	/**
	 * Description: Return true iff the senderID is connected.
	 * param1: void.
	 * retval: bool.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *
	 */
	IS_LOGGED_IN,
	
	/********************************** Shared employee & Customer commands **********************************/
	
	/**
	 * Description: Client command for getting the relevant catalog product represented by a barcode.
	 * param1: SmartCode - Smartcode with barcode and null on expertionDate.
	 * retval: CatalogProduct.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_CATALOG_PRODUCT_DOES_NOT_EXIST
	 *
	 */
	VIEW_PRODUCT_FROM_CATALOG,
	
	/**
	 * Description: Remove Customer from the system.
	 * param1: String - username.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_USERNAME_DOES_NOT_EXIST,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only a Manager can remove the Customer from system.
	 * 2. The command will disconnect the Customer from system if needed.
	 * 3. Can't remove "Guest" from system (will result in SM_INVALID_PARAMETER)
	 */
	REMOVE_CUSTOMER, 
	
	/********************************** Employee commands **********************************/
	
	/**
	 * Description: Employee add new ProductPackage to warehouse.
	 * param1: ProductPackage - The ProductPackage which will be add to warehouse.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_CATALOG_PRODUCT_DOES_NOT_EXIST,
	 *
	 */
	ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
	
	/**
	 * Description: Manager add new CatalogProduct to the market catalog.
	 * param1: CatalogProduct - The CatalogProduct which will be add to the market catalog.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_CATALOG_PRODUCT_ALREADY_EXISTS,
	 *
	 */
	ADD_PRODUCT_TO_CATALOG,
	
	/**
	 * Description: Manager remove CatalogProduct from the market catalog.
	 * param1: SmartCode - Smartcode with barcode and null on expertionDate.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_CATALOG_PRODUCT_DOES_NOT_EXIST,
	 *			SM_CATALOG_PRODUCT_STILL_FOR_SALE,
	 *
	 */
	REMOVE_PRODUCT_FROM_CATALOG,

	/**
	 * Description: Employee view ProductPackage from warehouse or shelves.
	 * param1: ProductPackage - The ProductPackage which will be add to warehouse.
	 * retval: Amount.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_PRODUCT_PACKAGE_DOES_NOT_EXIST,
	 *
	 */
	GET_PRODUCT_PACKAGE_AMOUNT,
	
	/**
	 * Description: Manager edit product from the catalog.
	 * param1: CatalogProduct - the new CatalogProduct content to be updated. 
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_CATALOG_PRODUCT_DOES_NOT_EXIST,
	 *
	 */
	EDIT_PRODUCT_FROM_CATALOG,

	/**
	 * Description: Employee move product package from warehouse to shelves.
	 * param1: ProductPackage - ProductPackage with SmartCode of the ProductPackage from the warehouse,
	 *                          amount to move and the new location of the ProductPackage.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_PRODUCT_PACKAGE_DOES_NOT_EXIST,
	 *			SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE,
	 *
	 */
	PLACE_PRODUCT_PACKAGE_ON_SHELVES,

	/**
	 * Description: Employee remove product package from warehouse or shelves.
	 * param1: ProductPackage - ProductPackage with SmartCode of the ProductPackage,
	 *                          amount to remove and the location of the ProductPackage.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_PRODUCT_PACKAGE_DOES_NOT_EXIST,
	 *			SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE,
	 *
	 */
	REMOVE_PRODUCT_PACKAGE_FROM_STORE,
	
	/**
	 * Description: Register new Worker to the system.
	 * param1: Login.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 * 			SM_SENDER_IS_NOT_CONNECTED
	 *			SM_USERNAME_ALREADY_EXISTS,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can register new worker. 
	 */
	REGISTER_NEW_WORKER,
	
	/**
	 * Description: Remove Worker from the system.
	 * param1: String - username.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_USERNAME_DOES_NOT_EXIST,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can remove worker.
	 * 2. Removing connected worker will disconnect the worker from system.
	 */
	REMOVE_WORKER,
	
	/**
	 * Description: Register new ingredient to the system.
	 * param1: Ingredient (only name is used)
	 * retval: new Ingredient with the given name and the id generated to it by server
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 * 			SM_SENDER_IS_NOT_CONNECTED,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can add new ingredient. 
	 */
	ADD_INGREDIENT,
	
	/**
	 * Description: Remove Ingredient from the system.
	 * param1: Ingredient.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			PARAM_ID_IS_NOT_EXIST,
	 *			SM_INGREDIENT_STILL_IN_USE,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can remove ingredient.
	 */
	REMOVE_INGREDIENT,
	
	/**
	 * Description: edit Ingredient, ID must exist in the system and will be updated according to param1
	 * param1: Ingredient.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			PARAM_ID_IS_NOT_EXIST,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can remove ingredient.
	 */
	EDIT_INGREDIENT,
	
	/**
	 * Description: Employee / Customer gets list of all ingredients.
	 * param1: void.
	 * retval: List of ingredients.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 * 
	 */
	
	GET_ALL_INGREDIENTS,
	
	/**
	 * Description: Add new manufacturer to the system.
	 * param1: Manufacturer.
	 * retval: new manufacturer with the given name and the id generated to it by server
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 * 			SM_SENDER_IS_NOT_CONNECTED,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can add new manufacturer. 
	 */
	ADD_MANUFACTURER,
	
	/**
	 * Description: Remove manufacturer to the system.
	 * param1: Manufacturer.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			PARAM_ID_IS_NOT_EXIST,
	 *			SM_MANUFACTURER_STILL_IN_USE,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can remove manufacturer.
	 */
	REMOVE_MANUFACTURER,
	
	/**
	 * Description: edit existing manufacturer - the ID must exists in the system and will be updated with new name
	 * param1: Manufacturer.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			PARAM_ID_IS_NOT_EXIST,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 *	 ***** NOTES *****
	 * 1. Only Manager can remove manufacturer.
	 */
	EDIT_MANUFACTURER,
	
	/**
	 * Description: Employee gets list of all manufacturers.
	 * param1: void.
	 * retval: List of manufacturers.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 */
	GET_ALL_MANUFACTURERS,
	
	/********************************** Customer commands **********************************/

	/**
	 * Description: Customer load it's own grocery list from the server.
	 * retval: GroceryList.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED
	 *
	 */
	LOAD_GROCERY_LIST,
	
	/**
	 * Description: Customer add product to grocery list.
	 * param1: ProductPackage - ProductPackage with SmartCode, amount and null on location.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_PRODUCT_PACKAGE_DOES_NOT_EXIST,
	 *			SM_PRODUCT_PACKAGE_NOT_ENOUGH_AMOUNT,
	 *			SM_INVALID_PARAMETER,
	 *
	 */
	ADD_PRODUCT_TO_GROCERY_LIST,

	/**
	 * Description: Customer remove product from grocery list.
	 * param1: ProductPackage - ProductPackage with SmartCode, amount and null on location.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_PRODUCT_PACKAGE_DOES_NOT_EXIST,
	 *			SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE,
	 *			SM_INVALID_PARAMETER,
	 *
	 */
	REMOVE_PRODUCT_FROM_GROCERY_LIST,

	/**
	 * Description: Customer checkout it's current active grocery list.
	 * param1: void.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_GROCERY_LIST_IS_EMPTY,
	 *
	 */
	CHECKOUT_GROCERY_LIST,
	
	/**
	 * Description: Register new Customer to the system.
	 * param1: CustomerProfile.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_USERNAME_ALREADY_EXISTS,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 */
	REGISTER_NEW_CUSTOMER,
		
	/**
	 * Description: Update Customer in the system. this command update any detail except to the password and ForgetPassword structure
	 * param1: CustomerProfile.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_USERNAME_DOES_NOT_EXIST,
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *	 		SM_INVALID_PARAMETER,
	 *
	 */
	UPDATE_CUSTOMER_PROFILE,
	
	/**
	 * Description: Checks if a given username is free (SM_OK on result code)
	 * param1: String.
	 * retval: void.
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_USERNAME_ALREADY_EXISTS,
	 *
	 */
	IS_FREE_CUSTOMER_NAME,
}
