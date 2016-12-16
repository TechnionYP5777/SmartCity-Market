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

	// cmdDescriptor:
	SM_INVALID_CMD_DESCRIPTOR,

	// params:
	SM_INVALID_PARAMETER,

	// sender:
	SM_INVALID_SENDER_ID,
	SM_SENDER_ID_DOES_NOT_EXIST,
	SM_SENDER_IS_NOT_CONNECTED,

	// catalog product:
	SM_CATALOG_PRODUCT_DOES_NOT_EXIST,

	// other:
	SM_ERR;
}
