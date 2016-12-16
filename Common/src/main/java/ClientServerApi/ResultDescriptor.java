package ClientServerApi;

public enum ResultDescriptor {
	/**
	 * These our result descriptors returned by the server. Add here more if
	 * needed. keep the convention!
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 */

// Success:
	SM_OK,

// Failure:

	// cmd:
	SM_INVALID_CMD_DESCRIPTOR,

	// params:
	SM_INVALID_PARAMETER,

	// login/logout:
	SM_INVALID_SENDER_ID,
	SM_SENDER_ID_DOES_NOT_EXIST,
	SM_SENDER_IS_NOT_CONNECTED,
	SM_SENDER_IS_ALREADY_CONNECTED,
	SM_USERNAME_DOES_NOT_EXIST,
	SM_WRONG_PASSWORD,
	

	// catalog product:
	SM_CATALOG_PRODUCT_DOES_NOT_EXIST,

	// other:
	SM_ERR;
}
