package SQLDatabase;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CartNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerStillUsed;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import SQLDatabase.SQLDatabaseException.WorkerAlreadyConnected;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;

public interface ISQLDatabaseConnection {

	int workerLogin(String username, String password)
			throws AuthenticationError, WorkerAlreadyConnected, CriticalError, NumberOfConnectionsExceeded;

	void workerLogout(Integer sessionID, String username) throws WorkerNotConnected, CriticalError;

	String getProductFromCatalog(Integer sessionID, long barcode)
			throws ProductNotExistInCatalog, WorkerNotConnected, CriticalError;

	void addProductPackageToWarehouse(Integer sessionID, ProductPackage p)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	void removeProductPackageFromWarehouse(Integer sessionID, ProductPackage p) throws CriticalError,
			WorkerNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void addProductToCatalog(Integer sessionID, CatalogProduct productToAdd)
			throws CriticalError, WorkerNotConnected, ProductAlreadyExistInCatalog, IngredientNotExist,
			ManufacturerNotExist;

	void removeProductFromCatalog(Integer sessionID, SmartCode productToRemove)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog, ProductStillForSale;

	void hardRemoveProductFromCatalog(Integer sessionID, CatalogProduct productToRemove)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	void editProductInCatalog(Integer sessionID, CatalogProduct productToUpdate)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog, IngredientNotExist,
			ManufacturerNotExist;

	int addManufacturer(Integer sessionID, String manufacturerName) throws CriticalError, WorkerNotConnected;

	void removeManufacturer(Integer sessionID, Manufacturer m)
			throws CriticalError, WorkerNotConnected, ManufacturerNotExist, ManufacturerStillUsed;

	void editManufacturer(Integer sessionID, Manufacturer newManufacturer)
			throws CriticalError, WorkerNotConnected, ManufacturerNotExist;

	void addProductToGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError, CartNotConnected,
			ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void removeProductFromGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError,
			CartNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void placeProductPackageOnShelves(Integer sessionID, ProductPackage productToBuy) throws CriticalError,
			WorkerNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void removeProductPackageFromShelves(Integer sessionID, ProductPackage productToBuy) throws CriticalError,
			WorkerNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	int getProductPackageAmonutOnShelves(Integer sessionID, ProductPackage productToBuy)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	int getProductPackageAmonutInWarehouse(Integer sessionID, ProductPackage productToBuy)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	void cartCheckout(Integer cartID) throws CriticalError, CartNotConnected;

	void close() throws CriticalError;

	void logoutAllUsers() throws CriticalError;

}