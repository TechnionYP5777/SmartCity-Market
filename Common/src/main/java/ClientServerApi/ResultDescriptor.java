package ClientServerApi;

public enum ResultDescriptor {
	/**
	 * These our result descriptors returned by the server. Add here more if
	 * needed. keep the convention!
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 * @author Aviad Cohen
	 */

/************************************************ Success ***************************************************/
	
	/**
	 * SM_OK
	 ********************************************************************************************************
	 *
	 * The command request accepted by the server and was executed successfully.
	 */
	SM_OK,

/************************************************ Failures **************************************************/

	/**************************************** General Failures **********************************************/
	
	/**
	 * SM_INVALID_CMD_DESCRIPTOR
	 ********************************************************************************************************
	 *
	 * The command request denied due to unsupported command by the server.
	 */
	SM_INVALID_CMD_DESCRIPTOR,

	/**
	 * SM_INVALID_PARAMETER
	 ********************************************************************************************************
	 *
	 * The command request has invalid parameter.
	 */
	SM_INVALID_PARAMETER,

	/**
	 * SM_INVALID_PARAMETER
	 ********************************************************************************************************
	 *
	 * The command request has invalid parameter.
	 */
	
	/**
	 * SM_ERR
	 ********************************************************************************************************
	 *
	 * The command request failed to general error (might be due to server initial failure).
	 *
	 */
	SM_ERR,

	/****************************************** Login/Logout failures **************************************/

	/**
	 * SM_INVALID_SENDER_ID
	 ********************************************************************************************************
	 *
	 * The command request has invalid sender id (Does not exist in the server database).
	 * 
	 * How to fix:
	 * 1. Send Login command.
	 */
	SM_INVALID_SENDER_ID,
	
	/**
	 * SM_SENDER_IS_NOT_CONNECTED
	 ********************************************************************************************************
	 *
	 * The sender is not connected to the system but tried to execute command still.
	 * 
	 * How to fix:
	 * 1. Send Login command.
	 */
	SM_SENDER_IS_NOT_CONNECTED,
	
	/**
	 * SM_SENDER_IS_ALREADY_CONNECTED
	 ********************************************************************************************************
	 *
	 * The sender tried to login to the server although he is already connected.
	 * 
	 * This might be caused due to:
	 * 1. Wrong sender id (which already exists on the system).
	 * 2. Synchronization problem with the server.
	 * 
	 * How to fix:
	 *
	 * 1. Send Logout command using the same sender id.
	 * 2. Send Login command.
	 */
	SM_SENDER_IS_ALREADY_CONNECTED,
	
	/**
	 * SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD
	 ********************************************************************************************************
	 *
	 * The sender tried to connect to the server with unknown username or with wrong password.
	 * 
	 * Due to security reasons the user shouldn't know if the username exists in the server database
	 * or he inserted a wrong password.
	 */
	SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD,
	
	/********************************** Shared failures for employee & cart ********************************/
	
	/**
	 * SM_CATALOG_PRODUCT_DOES_NOT_EXIST
	 ********************************************************************************************************
	 *
	 * The sender tried to get invalid product.
	 * 
	 * How to fix:
	 * 1. IF the smartcode was a valid smartcode - please add to the server database, otherwise - ignore.
	 */
	SM_CATALOG_PRODUCT_DOES_NOT_EXIST,
	
	/************************************** Employee failures only *****************************************/

	/***************************************** Cart failures only ******************************************/
}
