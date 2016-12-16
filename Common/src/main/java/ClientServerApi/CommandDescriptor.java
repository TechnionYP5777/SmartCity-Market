package ClientServerApi;

public enum CommandDescriptor {
	/**
	 * These are the commands we support: When adding a new command, make sure
	 * it's name is here also.
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 */

	// Connection:
	LOGIN,
	LOGOUT,

	// Catalog Product:
	VIEW_PRODUCT_FROM_CATALOG;
}