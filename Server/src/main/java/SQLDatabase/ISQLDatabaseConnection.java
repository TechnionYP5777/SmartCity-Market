package SQLDatabase;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.ProductPackage;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CartNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import SQLDatabase.SQLDatabaseException.WorkerAlreadyConnected;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;

public interface ISQLDatabaseConnection {

	int WorkerLogin(String username, String password)
			throws AuthenticationError, WorkerAlreadyConnected, CriticalError, NumberOfConnectionsExceeded;

	void WorkerLogout(Integer sessionID, String username) throws WorkerNotConnected, CriticalError;

	String getProductFromCatalog(Integer sessionID, long barcode)
			throws ProductNotExistInCatalog, WorkerNotConnected, CriticalError;

	void AddProductPackageToWarehouse(Integer sessionID, ProductPackage p)
			throws CriticalError, WorkerNotConnected;

	void RemoveProductPackageToWarehouse(Integer sessionID, ProductPackage p) throws CriticalError,
			WorkerNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void AddProductToCatalog(Integer sessionID, CatalogProduct productToAdd)
			throws CriticalError, WorkerNotConnected, ProductAlreadyExistInCatalog;

	void RemoveProductFromCatalog(Integer sessionID, CatalogProduct productToRemove)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog, ProductStillForSale;

	void HardRemoveProductFromCatalog(Integer sessionID, CatalogProduct productToRemove)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	void UpdateProductInCatalog(Integer sessionID, Long productBarcode, CatalogProduct productToUpdate)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	void AddProductToGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError, CartNotConnected,
			ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void RemoveProductFromGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError,
			CartNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void PlaceProductPackageOnShelves(Integer sessionID, ProductPackage productToBuy) throws CriticalError,
			WorkerNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	void RemoveProductPackageFromShelves(Integer sessionID, ProductPackage productToBuy) throws CriticalError,
			WorkerNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist;

	int GetProductPackageAmonutOnShelves(Integer sessionID, ProductPackage productToBuy)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	int GetProductPackageAmonutInWarehouse(Integer sessionID, ProductPackage productToBuy)
			throws CriticalError, WorkerNotConnected, ProductNotExistInCatalog;

	void cartCheckout(Integer cartID) throws CriticalError, CartNotConnected;

	void close() throws CriticalError;

}