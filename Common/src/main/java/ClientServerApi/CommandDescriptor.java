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
	 * ***** General Notes *****
	 * 
	 * 1. retval is relevant only on success (result_code = SM_OK).
	 * 2. retval will be stored in the data field of the CommandWrapper (unless maintained otherwise).
	 * 3. The Server/Client will interpret the data field according to the structure which is mentioned in
	 * the data field, the data will be stored in json.
	 * 4. When adding new commands, added reference in Github for all project contributes.
	 * 5. For each command there is option for SM_ERR - a ResultDescriptor for internal failure in the 
	 * server, such case must be considered always by the client side.
	 * 
	 * ****************************************************************************************************
	 * 
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 * @author Aviad Cohen
	 */

	/******************************************** Connection **********************************************/

	LOGIN,
	/**
	 * Description: Client login command to get in the system and receive unique sender id from server.
	 * param1: Login.
	 * retval: int senderId (in senderId field).
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
	 *	 ***** NOTE *****
	 * This command has no data from the server side.
	 */

	LOGOUT,
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
	 * 			SM_INVALID_SENDER_ID,
	 *			SM_SENDER_IS_NOT_CONNECTED,
	 *			SM_SENDER_ID_DOES_NOT_EXIST,
	 *
	 *	 ***** NOTE *****
	 * This command has no data from the server side.
	 */
	
	/********************************** Shared employee & cart commands **********************************/
	
	VIEW_PRODUCT_FROM_CATALOG;
	/**
	 * Description: Client command for getting the relevant catalog product represented by a barcode.
	 * param1: SmartCode - smartcode with barcode and null on expertionDate
	 * retval: CatalogProduct.
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