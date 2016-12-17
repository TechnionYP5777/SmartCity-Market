package ClientServerApi;

public enum CommandDescriptor {
	/**
	 * ****************************************************************************************************
	 * These are the commands we support:
	 *  When adding a new command, make sure you add it's name & description here as well.
	 * 
	 * ****************************************************************************************************
	 * KEEP THE CONVENTIONS!
	 * ****************************************************************************************************
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 */

	// Connection:
	LOGIN,
	/**
	 * Description: Client login command to get in the system and receive unique sender id from server.
	 * param1: String userName
	 * param2: String password
	 * retval: int senderId
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 *			SM_SENDER_IS_ALREADY_CONNECTED,
	 *			SM_USERNAME_DOES_NOT_EXIST,
	 *			SM_WRONG_PASSWORD,
	 *			SM_INVALID_PARAMETER,
	 *
	 */

	LOGOUT,
	/**
	 * Description: Client logout command for logging out of server.
	 * retval: void
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 * 			SM_INVALID_SENDER_ID,
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_SENDER_ID_DOES_NOT_EXIST,
	 *
	 */

	// Catalog Product:
	VIEW_PRODUCT_FROM_CATALOG;
	/**
	 * Description: Client command for getting the relevant catalog product represented by a barcode.
	 * param1: int barcode
	 * retval: CatlogProduct catProduct
	 *
	 * result_codes:
	 * 		success:
	 * 			SM_OK,
	 * 		
	 * 		failure:
	 * 			SM_INVALID_PARAMETER,
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_SENDER_ID_DOES_NOT_EXIST,
	 *			SM_CATALOG_PRODUCT_DOES_NOT_EXIST,
	 *
	 */
}