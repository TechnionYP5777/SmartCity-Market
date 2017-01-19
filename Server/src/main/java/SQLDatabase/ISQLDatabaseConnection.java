package SQLDatabase;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerStillUsed;
import SQLDatabase.SQLDatabaseException.NoGroceryListToRestore;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

public interface ISQLDatabaseConnection {

	int login(String username, String password)
			throws AuthenticationError, ClientAlreadyConnected, CriticalError, NumberOfConnectionsExceeded;

	String getClientType(Integer sessionID) throws ClientNotConnected, CriticalError;

	void logout(Integer sessionID, String username) throws ClientNotConnected, CriticalError;

	boolean isClientLoggedIn(Integer sessionID) throws CriticalError;

	boolean isWorkerLoggedIn(String username) throws CriticalError;

	String getProductFromCatalog(Integer sessionID, long barcode)
			throws ProductNotExistInCatalog, ClientNotConnected, CriticalError;

	void addProductPackageToWarehouse(Integer sessionID, ProductPackage p)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog;

	void removeProductPackageFromWarehouse(Integer sessionID, ProductPackage p) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void addProductToCatalog(Integer sessionID, CatalogProduct productToAdd) throws CriticalError, ClientNotConnected,
			ProductAlreadyExistInCatalog, IngredientNotExist, ManufacturerNotExist;

	void removeProductFromCatalog(Integer sessionID, SmartCode productToRemove)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog, ProductStillForSale;

	void hardRemoveProductFromCatalog(Integer sessionID, CatalogProduct productToRemove)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog;

	void editProductInCatalog(Integer sessionID, CatalogProduct productToUpdate) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, IngredientNotExist, ManufacturerNotExist;

	String addManufacturer(Integer sessionID, String manufacturerName) throws CriticalError, ClientNotConnected;

	void removeManufacturer(Integer sessionID, Manufacturer m)
			throws CriticalError, ClientNotConnected, ManufacturerNotExist, ManufacturerStillUsed;

	void editManufacturer(Integer sessionID, Manufacturer newManufacturer)
			throws CriticalError, ClientNotConnected, ManufacturerNotExist;
	
	String getManufacturersList(Integer sessionID) throws ClientNotConnected, CriticalError;

	void addProductToGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError, ClientNotConnected,
			ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void removeProductFromGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void placeProductPackageOnShelves(Integer sessionID, ProductPackage productToBuy) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void removeProductPackageFromShelves(Integer sessionID, ProductPackage productToBuy) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	String getProductPackageAmonutOnShelves(Integer sessionID, ProductPackage productToBuy)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog;

	String getProductPackageAmonutInWarehouse(Integer sessionID, ProductPackage productToBuy)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog;

	void cartCheckout(Integer cartID) throws CriticalError, ClientNotConnected;

	String cartRestoreGroceryList(Integer cartID) throws CriticalError, NoGroceryListToRestore;

	void close() throws CriticalError;

	void logoutAllUsers() throws CriticalError;

}